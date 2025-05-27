package com.esdllm.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esdllm.usercenter.common.ErrorCode;
import com.esdllm.usercenter.common.ResultUtils;
import com.esdllm.usercenter.contant.UserContant;
import com.esdllm.usercenter.exception.BusinessException;
import com.esdllm.usercenter.model.User;
import com.esdllm.usercenter.service.UserService;
import com.esdllm.usercenter.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author LiYehe
* &#064;description  针对表【user(用户表)】的数据库操作Service实现
* &#064;createDate  2024-11-22 19:51:09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "mySaltEsdllm";

    private final UserMapper userMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(UserMapper userMapper,RedisTemplate<String,Object> redisTemplate) {
        this.userMapper = userMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String inspectionCode) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,inspectionCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8");
        }
        if (inspectionCode.length()>5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"检验编号长度大于5");
        }
        //账户不能包含特殊字符
        String validPattern = "[ `~!#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……（）—【】\"‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }
        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //检验编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_code", inspectionCode);
        count = this.count(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"检验编号重复");
        }

        // 2. 加密
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setInspectionCode(inspectionCode);

        boolean saveResult = this.save(user);
        if(!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败");
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8");
        }
        //账户不能包含特殊字符
        String validPattern = "[ `~!#$%^&*()+=|{}':;,\\[\\].<>/?！@￥…（）—【】\"‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }
        // 2. 加密
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());

        // 3. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) {
            log.info(new Date() + "user login failed,userAccount can not match password");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
        }
        User safetyUser = getSafetyUser(user);

        // 4.记录用户的登录状态
        HttpSession session = request.getSession();
        session.setAttribute(UserContant.User_LOGIN_STATE, safetyUser);
        // 5. 返回脱敏后的用户信息
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser 原始用户
     * @return 脱敏后的用户
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setInspectionCode(originUser.getInspectionCode());
        safetyUser.setTags(originUser.getTags());
        safetyUser.setProfile(originUser.getProfile());
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserContant.User_LOGIN_STATE);
        return 1;
    }

    @Override
    public int deleteById(Long id) {
        if(id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User user = userMapper.selectById(id);
        if(user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        if(user.getUserRole() == 1) {
            throw new BusinessException(ErrorCode.NO_AUTH,"不能删除管理员");
        }

        int rows = userMapper.deleteById(id);
        if(rows > 0) {
            return rows;
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
    }

    /**
     * 根据标签搜索用户(内存过滤)
     *
     * @param tagNameList 用户必须要有的标签
     * @return 用户列表
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList){
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        userMapper.selectList(null);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>(){}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).toList();
    }

    @Override
    public int updateUser(User user, User loginUser) {
        Long userId = user.getId();
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断权限，仅管理员和自己可修改
        if (!isAdmin(loginUser)&&!userId.equals(loginUser.getId())){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Object userObj = request.getSession().getAttribute(UserContant.User_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        User currentUser = (User) userObj;
        return getSafetyUser(currentUser);
    }

    /**
     * 根据标签搜索用户SQL查询
     *
     * @param tagNameList 用户必须要有的标签
     * @return 用户列表
     */
    @Deprecated
    private List<User> searchUsersByTagsBySQL(List<String> tagNameList){
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //拼接 and 查询
        for (String tagName : tagNameList) {
            queryWrapper.like(User::getTags, tagName);
        }
        //查询
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSafetyUser).toList();
    }

    /**
     * 是否为管理员
     * @param request 请求
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserContant.User_LOGIN_STATE);
        User user = (User) userObject;
        return user!= null && user.getUserRole() == UserContant.ADMIN_ROLE;
    }

    @Override
    public Page<User> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {

        User loginUser = getLoginUser(request);
        //如果缓存里有，直接读缓存
        String redisKey = UserContant.redisKeyUser(loginUser.getId());
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>) opsForValue.get(redisKey);
        if(userPage != null&&pageNum>1) {
            return userPage;
        }
        //缓存中没有，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        userPage = page(new Page<>(pageNum,pageSize),queryWrapper);
        //缓存数据
        try {
            opsForValue.set(redisKey,userPage,1000*60*5, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return userPage;
    }

    /**
     * 是否为管理员
     * @param loginUser 请求
     * @return 是否为管理员
     */
    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser!= null && loginUser.getUserRole() == UserContant.ADMIN_ROLE;
    }
}




