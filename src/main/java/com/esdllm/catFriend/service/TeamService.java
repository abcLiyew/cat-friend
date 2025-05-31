package com.esdllm.catFriend.service;

import com.esdllm.catFriend.model.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.model.dto.TeamQuery;
import com.esdllm.catFriend.model.request.TeamUpdateRequest;
import com.esdllm.catFriend.model.vo.TeamUserVo;

import java.util.List;

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

    /**
     * 获取队伍列表
     *
     * @param teamQuery 队伍查询参数
     * @param isAdmin
     * @return 队伍列表
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest 更新队伍参数
     * @param loginUser 登录用户
     * @return 是否成功
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);
}
