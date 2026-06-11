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
@WebFilter(urlPatterns = "/users/*")
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
            //1.获取路径
            String path = request.getRequestURI();
            //2.判断是否为登录操作
            if(Objects.equals(path,"/users/login") || Objects.equals(path,"/users/register")){
                log.info("登录或注册");
                filterChain.doFilter(request,response);
                return;
            }
            //3.校验token
            //获取token
            else{
                String token = request.getHeader("token");
                if(token == null || token.isEmpty()){
                    log.info("token 为空");
                    response.setStatus(401);
                    return;
                }
                else{
                    log.info("校验 token");
                    try {
                        //存入 id 数据
                        Claims claims = JwtUtil.parseTokenUser(token);
                        Integer id = Integer.valueOf(claims.getId());
                        Context.setUserId(id);
                        //放行
                        filterChain.doFilter(request,response);
                    }catch (Exception e) {
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
