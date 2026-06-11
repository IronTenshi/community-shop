package com.Fanggaozhiai.filter;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@WebFilter(urlPatterns = "/emps/*")
public class EmployeePermissionFilter implements Filter {

    //初始化
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化过滤器");
    }
    //过滤器
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            //获取路径
            String path = request.getRequestURI();
            //2.判断请求路径是否是登录
            if(Objects.equals(path, "/login")){
                log.info("登录放行,获取token");
                filterChain.doFilter(request, response);
                return;
            }
            //3.判断token
            else {
                //获取 token
                String token = request.getHeader("token");

                if (token == null || token.isEmpty()) {
                    log.info("employee 未登录");
                    response.setStatus(401);
                    return;
                }
                //校验 token
                else {
                    try {
                        //解析token
                        Claims claims = JwtUtil.parseTokenEmp(token);
                        //获取用户id
                        Integer id = Integer.valueOf(claims.getId());
                        Context.setUserId(id);
                        //令牌合法放行
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        //响应错误
                        response.setStatus(401);
                        return;
                    }
                }
            }
        }finally {
            Context.clear();
        }
    }
    @Override
    public void destroy() {
        log.info("销毁过滤器");
    }
}
