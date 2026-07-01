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

/**
 * 用户权限过滤器
 * 拦截 /user/*、/shops/*、/users/products/*、/users/orders/* 路径，校验用户token
 * 放行 /user/login 和 /user/register 端点
 * 校验通过后，将用户ID存入ThreadLocal（Context），供后续业务使用
 */
@Slf4j
@WebFilter(urlPatterns = {"/user/*", "/shops/*", "/shops", "/users/*"})
public class UserPermissionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化用户权限过滤器");
    }

    /**
     * 过滤器核心逻辑
     * 1. 放行登录和注册接口
     * 2. 从请求头获取token并校验合法性
     * 3. 解析token获取用户ID，存入ThreadLocal
     * 4. 放行请求
     * 5. finally中清除ThreadLocal，防止内存泄漏
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
            // 解析token
            Claims claims = JwtUtil.parseTokenUser(token);
            Integer id = getUserId(claims);
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

    /**
     * 从JWT claims中提取用户ID
     *
     * @param claims JWT载荷
     * @return 用户ID
     */
    private Integer getUserId(Claims claims) {
        return claims.get("id", Integer.class);
    }

    @Override
    public void destroy() {
        log.info("销毁用户权限过滤器");
    }
}