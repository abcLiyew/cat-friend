package com.esdllm.catFriend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.esdllm.catFriend.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 根据标签搜索用户
     *
     * @param tagsList 标签列表
     * @return 用户列表
     */
    List<User> searchUsersByTags(List<String> tagsList);
    /**
     * 更新用户信息
     *
     * @param user      用户信息
     * @param loginUser
     * @return 更新结果
     */
    int updateUser(User user, User loginUser);

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    Page<User> recommendUsers(long pageSize, long pageNum, HttpServletRequest request);

    boolean isAdmin(User loginUser);
}
