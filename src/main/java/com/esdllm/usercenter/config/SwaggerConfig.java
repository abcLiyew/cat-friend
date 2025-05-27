package com.esdllm.usercenter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("admin-api")         // 分组名称
                .pathsToMatch("/**")  // 接口请求路径规则
                .build();
    }
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info() // 基本信息配置
                        .title("用户中心接口文档Knife4j") // 标题
                        .description("用户中心对外暴露的接口") // 描述Api接口文档的基本信息
                        .version("1.0.0") // 版本
                        // 设置OpenAPI文档的联系信息，包括联系人姓名为"robin"，邮箱为"robin@gmail.com"。
                        .contact(new Contact().name("饿死的流浪猫").email("18378500963@163.com").url("https://github.com/abcLiyew"))
                        // 设置OpenAPI文档的许可证信息，包括许可证名称为"MIT"，许可证URL为"http://springdoc.org"。
                        .license(new License().name("MIT").url("https://mit-license.org/"))
                );

    }
}
