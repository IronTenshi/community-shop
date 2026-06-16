package com.Fanggaozhiai.utils;

import com.Fanggaozhiai.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    //注入属性
    //注入属性不能注入static变量
    @Autowired
    private JwtConfig jjwtConfig;

    private static JwtConfig jwtConfig;
    @PostConstruct
    public void init(){
        jwtConfig = this.jjwtConfig;
    }
    /**
     * 获取token
     * @param claims
     * @return
     */
    public static String getTokenEmp(Map<String,Object> claims){
        //获取固定密钥
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getEmployee()));
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(key)
                .compact();
    }
    /**
     * 解析token
     * @param token
     * @return
     *  */
    public static Claims parseTokenEmp(String token) {
        //获取固定密钥
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getEmployee()));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
                .getPayload();
    }

    public static String getTokenUser(Map<String,Object> claims){
        //获取固定密钥
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getUser()));
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpire()))
                .signWith(key)
                .compact();
    }
    public static Claims parseTokenUser(String token) {
        //获取固定密钥
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getUser()));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
                .getPayload();
    }
}
