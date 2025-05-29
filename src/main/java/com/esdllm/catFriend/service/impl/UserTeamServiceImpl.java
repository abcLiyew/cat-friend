package com.esdllm.catFriend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esdllm.catFriend.model.UserTeam;
import com.esdllm.catFriend.service.UserTeamService;
import com.esdllm.catFriend.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author LiYehe
* @description 针对表【user_team(用户-队伍关系表)】的数据库操作Service实现
* @createDate 2025-05-27 17:20:04
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




