package com.Fanggaozhiai.filter;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.mapper.EmployeeMapper;
import com.Fanggaozhiai.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

/**
 * 用户权限过滤器
 * 拦截 /user/*、/shops/*、/users/* 路径，校验token
 * 放行 /user/login 和 /user/register 端点
 * 支持用户token和管理员token（管理员可拥有用户操作权限）
 * 校验通过后，将用户/管理员ID存入ThreadLocal（Context），供后续业务使用
 */
@Slf4j
@WebFilter(urlPatterns = {"/user/*", "/shops/*", "/shops", "/users/*"})
public class UserPermissionFilter implements Filter {

    private EmployeeMapper employeeMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 通过Spring容器获取Mapper Bean（Filter不是Spring管理的Bean）
        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.employeeMapper = applicationContext.getBean(EmployeeMapper.class);
        log.info("初始化用户权限过滤器");
    }

    /**
     * 过滤器核心逻辑
     * 1. 放行登录和注册接口
     * 2. 从请求头获取token，先尝试用户token解析，失败则尝试管理员token解析
     * 3. 管理员token需验证 job=0（管理员）才有用户操作权限
     * 4. 解析token获取ID，存入ThreadLocal
     * 5. 放行请求
     * 6. finally中清除ThreadLocal，防止内存泄漏
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入用户权限过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        try {
            // 放行 OPTIONS 预检请求，让 Spring MVC CORS 配置处理
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }
            // 放行登录和注册操作
            if (path.equals("/user/login") || path.equals("/user/register")) {
                filterChain.doFilter(request, response);
                return;
            }
            // 获取token
            String token = request.getHeader("token");
            // 验证token合法性
            if (token == null || token.isBlank()) {
                log.info("token为空或不存在");
                setErrorResponse(response, 401);
                return;
            }
            // 先尝试解析用户token
            Integer id = null;
            try {
                Claims claims = JwtUtil.parseTokenUser(token);
                id = claims.get("id", Integer.class);
                log.info("用户token验证通过，用户ID: {}", id);
            } catch (Exception e) {
                // 用户token解析失败，尝试解析管理员token
                log.info("用户token解析失败，尝试管理员token: {}", e.getMessage());
                try {
                    Claims claims = JwtUtil.parseTokenEmp(token);
                    Integer empId = claims.get("id", Integer.class);
                    // 验证员工存在性及是否为管理员
                    Employee employee = employeeMapper.selectById(empId);
                    if (employee == null) {
                        log.info("员工不存在");
                        setErrorResponse(response, 401);
                        return;
                    }
                    if (employee.getJob() != 0) {
                        log.info("非管理员员工无权限访问用户资源，job={}", employee.getJob());
                        setErrorResponse(response, 403);
                        return;
                    }
                    id = empId;
                    log.info("管理员token验证通过，管理员ID: {}", id);
                } catch (Exception e2) {
                    log.error("token解析失败（用户和管理员均失败）: {}", e2.getMessage());
                    setErrorResponse(response, 401);
                    return;
                }
            }
            // 存入上下文工具
            Context.setId(id);
            // 放行请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("用户权限校验失败 - token解析异常: {}", e.getMessage(), e);
            setErrorResponse(response, 401);
        } finally {
            Context.clear();
        }
    }

    /**
     * 设置错误响应（含CORS头，防止浏览器拦截401/403响应）
     */
    private void setErrorResponse(HttpServletResponse response, int status) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setStatus(status);
    }

    @Override
    public void destroy() {
        log.info("销毁用户权限过滤器");
    }
}