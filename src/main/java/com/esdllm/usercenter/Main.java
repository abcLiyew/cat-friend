package com.esdllm.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}