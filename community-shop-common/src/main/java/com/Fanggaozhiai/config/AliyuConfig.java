package com.Fanggaozhiai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类
 * 读取application.yml中aliyun.oss前缀的配置属性
 * 包含OSS访问域名、存储桶名称和地域信息
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyuConfig {
    /** OSS访问域名 */
    private String endpoint;

    /** 存储桶名称 */
    private String bucketName;

    /** OSS地域 */
    private String region;
}
