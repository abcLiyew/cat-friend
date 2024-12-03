package com.esdllm.usercenter.mapper;

import com.esdllm.usercenter.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author LiYehe
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-11-29 20:26:03
* @Entity com.esdllm.usercenter.model.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




