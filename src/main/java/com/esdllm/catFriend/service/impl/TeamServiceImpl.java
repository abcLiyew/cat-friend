package com.esdllm.catFriend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esdllm.catFriend.model.Team;
import com.esdllm.catFriend.service.TeamService;
import com.esdllm.catFriend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author LiYehe
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2025-05-27 17:20:54
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




