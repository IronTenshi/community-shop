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

@Slf4j
@WebFilter(urlPatterns = {"/admin/*", "/delivery/*"})
public class EmployeePermissionFilter implements Filter {

    private EmployeeMapper employeeMapper;

    //初始化
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.employeeMapper = applicationContext.getBean(EmployeeMapper.class);
        log.info("初始化过滤器");
    }
    //过滤器
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        try {
            //获取token
            String token = request.getHeader("token");

            //验证token合法性
            if(token == null || token.isBlank()){
                log.info("token为空或不存在");
                response.setStatus(401);
                return;
            }
            //提取token信息
            Claims claims = JwtUtil.parseTokenEmp(token);
            Integer id = getEmpId(claims);

            //验证token
            Employee employee = employeeMapper.selectById(id);
            if(employee == null){
                log.info("用户不存在");
                response.setStatus(401);
                return;
            }

            //设置当前用户ID到上下文
            //以便于后续业务逻辑使用
            Context.setId(id);

            //admin层验证信息job = 0
            if(path.startsWith("/admin")){
                if(employee.getJob() != 0){
                    log.info("用户权限不足");
                    response.setStatus(403);
                    return;
                }
            }

            //验证通过，放行请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("员工权限校验失败", e);
            response.setStatus(401);
        } finally {
            Context.clear();
        }
    }

    //从token中获取用户id
    private Integer getEmpId(Claims claims){
        return claims.get("id", Integer.class);
    }

    @Override
    public void destroy() {
        log.info("销毁过滤器");
    }
}
