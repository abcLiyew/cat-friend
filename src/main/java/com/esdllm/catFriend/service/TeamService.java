package com.esdllm.catFriend.service;

import com.esdllm.catFriend.model.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.esdllm.catFriend.model.User;

/**
* @author LiYehe
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2025-05-27 17:20:54
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team 队伍信息
     * @param loginUser  创建人
     * @return 队伍id
     */
    long addTeam(Team team, User loginUser);
}
