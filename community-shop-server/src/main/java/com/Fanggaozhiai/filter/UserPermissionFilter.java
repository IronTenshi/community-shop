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

@Slf4j
@WebFilter(urlPatterns = {"/user/*", "/shops/*", "/users/products/*","/users/orders/*"})
public class UserPermissionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化过滤器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            //获取token
            String token = request.getHeader("token");
            //验证token合法性
            if (token == null || token.isBlank()){
                log.info("token为空或不存在");
                response.setStatus(401);
                return;
            }
            //验证token
            Claims claims = JwtUtil.parseTokenUser(token);
            Integer id = getUserId(claims);
            //存入上下文工具
            Context.setId(id);
            //放行请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Context.clear();
        }
    }

    //从上下文中获取id
    private Integer getUserId(Claims claims){
        return claims.get("id", Integer.class);
    }

    @Override
    public void destroy() {
        log.info("销毁过滤器");
    }
}
