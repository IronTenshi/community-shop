package com.Fanggaozhiai.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    //注入属性
    @NotBlank(message = "secret不能为空")
    private String secret;
    @NotNull(message = "expire不能为空")
    private long expire;
}
