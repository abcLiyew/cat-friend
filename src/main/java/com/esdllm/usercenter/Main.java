package com.esdllm.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 类名 : ${NAME}
 * 包 : com.esdllm
 * 描述 :
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/21 23:20
 * @version 1.0.1
 */
@SpringBootApplication
@MapperScan("com.esdllm.usercenter.mapper")
@EnableRedisHttpSession
@EnableScheduling
public class Main  implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    // 跨域问题解决
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .maxAge(3600)
                .allowedHeaders("Origin", "Accept", "Content-Type", "Authorization")
                .allowCredentials(true);
    }
}