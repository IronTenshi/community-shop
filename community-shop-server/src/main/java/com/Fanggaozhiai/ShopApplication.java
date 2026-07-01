package com.Fanggaozhiai;

import com.Fanggaozhiai.config.AliyuConfig;
import com.Fanggaozhiai.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ServletComponentScan
//从 Spring Boot 2.2+
//开始，如果配置类本身标注了
//@Component，则无需显式使用 @EnableConfigurationProperties。
//@EnableConfigurationProperties({JwtConfig.class})
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

}