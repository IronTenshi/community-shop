package com.Fanggaozhiai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CORS配置
 * 读取application.yml中的cors配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
    List<String> allowedOrigin;
}