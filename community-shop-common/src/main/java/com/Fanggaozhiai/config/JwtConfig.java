package com.Fanggaozhiai.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置类
 * 读取application.yml中jwt前缀的配置属性
 * 包含员工密钥、用户密钥和令牌过期时间
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /** 员工JWT签名密钥 */
    @NotBlank(message = "secret不能为空")
    private String employee;

    /** 令牌过期时间（毫秒） */
    @NotNull(message = "expire不能为空")
    private long expire;

    /** 用户JWT签名密钥 */
    @NotBlank(message = "user不能为空")
    private String user;
}
