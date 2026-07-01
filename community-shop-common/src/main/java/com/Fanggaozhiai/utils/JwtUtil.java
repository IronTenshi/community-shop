package com.Fanggaozhiai.utils;

import com.Fanggaozhiai.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 负责生成和解析JWT令牌，区分员工和用户两种密钥
 * 使用@PostConstruct将Spring注入的配置属性复制到静态变量中
 */
@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private JwtConfig jjwtConfig;

    private static JwtConfig jwtConfig;

    /**
     * 初始化静态变量
     * 将Spring注入的配置属性复制到静态变量，以便静态方法使用
     */
    @PostConstruct
    public void init() {
        jwtConfig = this.jjwtConfig;
        log.info("JwtUtil初始化完成, userSecret={}, expire={}", jwtConfig.getUser() != null ? "已加载" : "null", jwtConfig.getExpire());
    }

    /**
     * 生成员工JWT令牌
     *
     * @param claims JWT载荷，包含员工ID等信息
     * @return JWT令牌字符串
     */
    public static String getTokenEmp(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getEmployee()));
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(key)
                .compact();
    }

    /**
     * 解析员工JWT令牌
     *
     * @param token JWT令牌字符串
     * @return JWT载荷（Claims）
     */
    public static Claims parseTokenEmp(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getEmployee()));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    /**
     * 生成用户JWT令牌
     *
     * @param claims JWT载荷，包含用户ID等信息
     * @return JWT令牌字符串
     */
    public static String getTokenUser(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getUser()));
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(key)
                .compact();
    }

    /**
     * 解析用户JWT令牌
     *
     * @param token JWT令牌字符串
     * @return JWT载荷（Claims）
     */
    public static Claims parseTokenUser(String token) {
        if (jwtConfig == null) {
            log.error("JwtUtil.parseTokenUser: jwtConfig为null，请检查JwtUtil是否已被Spring初始化");
            throw new IllegalStateException("JWT配置未初始化");
        }
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getUser()));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}