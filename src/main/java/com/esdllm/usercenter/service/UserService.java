package com.esdllm.usercenter.service;

import com.esdllm.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * @author esdllm
 * @author LiYehe
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-11-22 19:51:09
 */
@Service
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param inspectionCode 检验编号
     * @return 新用户的id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String inspectionCode);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser 原始用户
     * @return 脱敏后的用户
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request HTTPRequest对象, 用于获取session对象
     * @return 0 表示成功, 非0 表示失败
     */
    int userLogout( HttpServletRequest request );

    int deleteById(Long id);
}
