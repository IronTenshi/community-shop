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
 * 员工权限过滤器
 * 拦截 /admin/*、/delivery/*、/ 路径，校验员工token和角色权限
 * /admin/* 需要管理员权限（job = 0）
 * /delivery/* 需要员工登录即可
 * 校验通过后，将员工ID存入ThreadLocal（Context），供后续业务使用
 */
@Slf4j
@WebFilter(urlPatterns = {"/admin/*", "/delivery/*", "/"})
public class EmployeePermissionFilter implements Filter {

    private EmployeeMapper employeeMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 通过Spring容器获取Mapper Bean（Filter不是Spring管理的Bean）
        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.employeeMapper = applicationContext.getBean(EmployeeMapper.class);
        log.info("初始化员工权限过滤器");
    }

    /**
     * 过滤器核心逻辑
     * 1. 从请求头获取token并校验合法性
     * 2. 解析token获取员工ID，查询数据库验证员工存在性
     * 3. /admin/* 路径额外校验 job = 0（管理员）
     * 4. 将员工ID存入ThreadLocal
     * 5. 放行请求
     * 6. finally中清除ThreadLocal，防止内存泄漏
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入员工权限过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 放行 OPTIONS 预检请求，让 Spring MVC CORS 配置处理
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 获取token
            String token = request.getHeader("token");

            // 验证token合法性
            if (token == null || token.isBlank()) {
                log.info("token为空或不存在");
                setErrorResponse(response, 401);
                return;
            }
            // 解析token
            Claims claims = JwtUtil.parseTokenEmp(token);
            Integer id = getEmpId(claims);

            // 验证员工存在性
            Employee employee = employeeMapper.selectById(id);
            if (employee == null) {
                log.info("员工不存在");
                setErrorResponse(response, 401);
                return;
            }

            // 设置当前用户ID到上下文
            Context.setId(id);

            // admin层验证：只有管理员（job = 0）才能访问
            if (path.startsWith("/admin")) {
                if (employee.getJob() != 0) {
                    log.info("用户权限不足，需要管理员权限");
                    setErrorResponse(response, 403);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("员工权限校验失败", e);
            setErrorResponse(response, 401);
            return;
        }

        // 验证通过，放行请求（业务异常由Spring全局异常处理器处理）
        try {
            filterChain.doFilter(request, response);
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

    /**
     * 从JWT claims中提取员工ID
     *
     * @param claims JWT载荷
     * @return 员工ID
     */
    private Integer getEmpId(Claims claims) {
        return claims.get("id", Integer.class);
    }

    @Override
    public void destroy() {
        log.info("销毁员工权限过滤器");
    }
}