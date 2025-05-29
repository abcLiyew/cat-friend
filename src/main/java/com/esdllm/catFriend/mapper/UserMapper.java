package com.esdllm.catFriend.mapper;

import com.esdllm.catFriend.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author LiYehe
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-11-29 20:26:03
* @Entity com.esdllm.usercenter.model.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




