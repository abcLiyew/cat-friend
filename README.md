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
### 用户注册
- URL: `/api/user/register`
- 方法: `POST`
- 请求体:
```json
{
  "username": "test", #用户名
  "password": "123456",#密码
  "checkPassword": "123456",#确认密码
  "instructionCode": 2 #检验码
}
```
- 响应实例：
```json
{
  "code": 200,
  "message": "注册成功",
  "data": 123456789
}
```
### 用户登录
- URL: `/api/user/login`
- 方法: `POST`
- 请求体:
- ```json
  {
    "username": "test", #用户名
    "password": "123456" #密码
  }
  ```
  - 响应实例：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userPassword": "******",
    "phone": null,
    "checkPassword": null,
    "inspectionCode": null,
    "createTime": "2024-01-19T09:38:21",
    "updateTime": "2024-01-19T09:38:21",
    "isDelete": 0,
    "role": 0,
    "profilePhoto": null,
    "introduction": null,
    "tags": null,
    "email": null,
    "age": null,
    "sex": null,
    "username": "testuser"
  }
}
```
### 用户登出
- URL: `/api/user/logout`
- 方法: `POST`
- 请求体: 无
- 响应实例：
```json
{
  "code": 200,
  "message": "登出成功",
  "data": true
}
```
### 用户搜索
- URL: `/api/user/search`
- 方法: `GET`
- 请求参数: 
| 参数名 | 类型 | 是否必填 |描述|
|userName|String|是|用户名|
- 响应实例：
```json
{
  "code": 200,
  "message": "搜索成功",
  "data": [
    {
      "id": 123456789,
      "userAccount": "testuser",
      "userPassword": "******",
      "phone": null,
      "checkPassword": null,
      "inspectionCode": null,
      "createTime": "2024-01-19T09:38:21",
      "updateTime": "2024-01-19T09:38:21",
      "isDelete": 0,
      "role": 0,
      "profilePhoto": null,
      "introduction": null,
      "tags": null,
      "email": null,
      "age": null,
      "sex": null,
      "username": "testuser"
    },
    {
      "id": 987654321,
      "userAccount": "testuser2",
      "userPassword": "******",
      "phone": null,
      "checkPassword": null,
      "inspectionCode": null,
      "createTime": "2024-01-19T09:38:21",
      "updateTime": "2024-01-19T09:38:21",
      "isDelete": 0,
      "role": 0,
      "profilePhoto": null,
      "introduction": null,
      "tags": null,
      "email": null,
      "age": null,
      "sex": null,
      "username": "testuser2"
    }
  ]
}
```
### 删除用户
- URL: `/api/user/delete`
- 方法: `POST`
- 请求体:
```json
{
  "id": 123456789 #用户id
}
```
- 响应实例：
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 获取当前登录用户
- URL: `/api/user/current`
- 方法: `GET`
- 请求参数: 无
- 响应实例：
```json
{
  "code": 200,
  "message": "ok",
  "data": {
    "id": 123456789,
    "userAccount": "testuser",
    "userPassword": null,
    "phone": null,
    "checkPassword": null,
    "inspectionCode": null,
    "createTime": "2024-01-19T09:38:21",
    "updateTime": "2024-01-19T09:38:21",
    "isDelete": 0,
    "role": 0,
    "profilePhoto": null,
    "introduction": null,
    "tags": null,
    "email": null,
    "age": null,
    "sex": null,
    "username": "testuser"
  }
}

```
  
