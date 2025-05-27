# 用户中心API文档

## 简介
本项目是一个用户中心API文档，提供用户的注册、登录、登出、搜索、删除等功能。

## 环境配置
- JDK17及以上
- SpringBoot3
- MySQL8.0
- Maven3.9.0
- Mybatis Plus3.5.8
- druid1.2.23
- Lombok
 ## 项目结构
```plaintext
user-center/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── esdllm/
│                   └── usercenter/
│                       ├── controller/
│                       │   └── UserController.java
│                       ├── model/
│                       │   ├── User.java
│                       │   └── request/
│                       │       ├── UserLoginRequest.java
│                       │       └── UserRegisterRequest.java
│                       ├── service/
│                       │   └── UserService.java
│                       └── utils/
│                           └── ResultUtils.java
└── README.md

```
## 安装与运行
1. 克隆项目到本地：
```bash
git clone https://github.com/abcLiyew/usercenter.git
```
2.安装依赖：
```bash
mvn clean install
```
3. 配置数据库连接：
在`application.yaml`文件中配置数据库连接信息：
```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql:///esdllm
      username: root
      password: aa152773652
      driver-class-name: com.mysql.cj.jdbc.Driver
```
4. 运行项目：
```bash
mvn spring-boot:run
```
或者使用IDE运行项目：
打开/src/main/java/com/esdllm/usercenter/Main.java文件，运行main方法。
## API 文档
> 用户中心前端请点击<a href=https://github.com/abcLiyew/usercenter-font>此处</a>

**api文档可以在项目启动后访问：http://[服务器IP]:8080/api/doc.html**
  
