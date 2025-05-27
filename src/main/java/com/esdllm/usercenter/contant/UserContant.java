package com.esdllm.usercenter.contant;

/**
 * 类名 : UserContant
 * 包 : com.esdllm.usercenter.contant
 * 描述 : 用户常量
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/26 21:47
 * @version 1.0.1
 */
public interface UserContant {
    /**
     * 用户登录态键
     */
    String User_LOGIN_STATE = "userLoginState";
    /**
     * 管理员权限, 1 表示管理员  0 表示默认权限
     */
    int ADMIN_ROLE = 1;
    int DEFAULT_ROLE = 0;

    static String redisKeyUser(Long userId){
        return String.format("catFriend:user:recommend:%s", userId);
    };
}
