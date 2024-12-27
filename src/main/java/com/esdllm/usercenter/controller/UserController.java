package com.esdllm.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.esdllm.usercenter.common.BaseResponse;
import com.esdllm.usercenter.common.ErrorCode;
import com.esdllm.usercenter.common.ResultUtils;
import com.esdllm.usercenter.contant.UserContant;
import com.esdllm.usercenter.exception.BusinessException;
import com.esdllm.usercenter.model.User;
import com.esdllm.usercenter.model.request.UserLoginRequest;
import com.esdllm.usercenter.model.request.UserRegisterRequest;
import com.esdllm.usercenter.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * 类名 : UserController
 * 包 : com.esdllm.usercenter.controller
 * 描述 :
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/26 20:17
 * @version 1.0.1
 */

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174","http://localhost:3000"},allowCredentials = "true")
public class UserController {
    /**
     * 用户服务对象，用于处理用户相关的业务逻辑
     */
    @Resource
    private UserService userService;

        /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册请求对象，包含用户账号、密码和确认密码
     * @return 注册成功返回用户ID，注册失败返回null
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 检查请求对象是否为空
        if (userRegisterRequest == null) {
        //    return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        // 获取请求中的用户账号、密码和确认密码
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String inspectionCode = userRegisterRequest.getInspectionCode();
        // 检查用户账号、密码和确认密码是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, inspectionCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        // 调用用户服务的注册方法，返回注册成功的用户ID
        Long result = userService.userRegister(userAccount, userPassword, checkPassword,
                inspectionCode);
        return ResultUtils.success(result);
    }

       /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录请求对象，包含用户账号和密码
     * @param request HttpServletRequest对象，用于获取session
     * @return 登录成功返回用户对象，登录失败返回null
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 检查请求对象是否为空
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        // 获取请求中的用户账号和密码
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 检查用户账号和密码是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        // 调用用户服务的登录方法，返回登录成功的用户对象
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

       /**
     * 用户登出接口
     *
     * @param request HttpServletRequest对象，用于获取session
     * @return 登出成功返回true，登出失败返回false
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        // 检查请求对象是否为空
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        //TODO 校验用户是否合法

        // 调用用户服务的登出方法，返回登录成功的用户对象
        int rows = userService.userLogout(request);
        return ResultUtils.success(rows);
    }

       /**
     * 用户搜索接口
     *
     * @param username 用户名，模糊搜索
     * @param request HttpServletRequest对象，用于获取session
     * @return 搜索到的用户列表
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request) {
        //鉴权，仅管理员可查询
        if(!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"没有管理员权限");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)) {
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().peek(user -> userService.getSafetyUser(user)).toList();
        return ResultUtils.success(list);
    }

      /**
     * 删除用户接口
     *
     * @param id 用户id
     * @param request HttpServletRequest对象，用于获取session
     * @return 删除成功返回true，删除失败返回false
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        //鉴权，仅管理员可查询
        if(!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH,"没有删除权限");
        }
        //检查id是否合法
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id为空！");
        }
        //调用用户服务的删除方法，返回删除结果
        int rows = userService.deleteById(id);
        boolean byId = rows > 0;
        if(!byId) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败！");
        }
        return ResultUtils.success(true);
    }

    /**
     *获取当前用户登录状态
     * @param request HttpServletRequest对象，用于获取session
     * @return 当前登录用户对象
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser( HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserContant.User_LOGIN_STATE);
        User currentUser = (User) userObject;
        if(currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        Long userId = currentUser.getId();
        //TODO 检验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        //将新查到的数据更新到session中
        request.getSession().setAttribute(UserContant.User_LOGIN_STATE,safetyUser);
        return ResultUtils.success(safetyUser);
    }


    /**
     * 是否为管理员
     * @param request 请求
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserContant.User_LOGIN_STATE);
        User user = (User) userObject;
        return user!= null && user.getUserRole() == UserContant.ADMIN_ROLE;
    }
}
