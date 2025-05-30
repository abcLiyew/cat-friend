package com.esdllm.catFriend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esdllm.catFriend.common.ErrorCode;
import com.esdllm.catFriend.exception.BusinessException;
import com.esdllm.catFriend.model.Team;
import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.model.UserTeam;
import com.esdllm.catFriend.model.enums.TeamStatusEnum;
import com.esdllm.catFriend.service.TeamService;
import com.esdllm.catFriend.mapper.TeamMapper;
import com.esdllm.catFriend.service.UserTeamService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
* @author LiYehe
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2025-05-27 17:20:54
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

    @Resource
    UserTeamService userTeamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
//        1. 请求参数是否为空？
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        2. 是否登录，未登录不允许创建
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
//        3. 校验信息：
//            1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
//            2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) ||  name.length() > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不满足要求");
        }
//            3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isBlank(description) && description.length() > 512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述不满足要求");
        }
//            4. status 是否公开(int),不传默认为0(公开)
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态不支持");
        }
//            5. 如果status为加密状态，一定要有密码，且密码长度 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)){
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不满足要求");
            }
        }
//            6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null && new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"超时时间 > 当前时间");
        }
//            7. 校验用户最多创建的队伍数量（5个）
        //TODO 有bug
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        final long userId = loginUser.getId();
        queryWrapper.eq(Team::getUserId, userId);
        long hasTeamCount = this.count(queryWrapper);
        if (hasTeamCount >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建5个队伍");
        }
//            8. TODO 校验用户最多加入的队伍数量（5个）
//        4. 插入数据到队伍信息表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建队伍失败");
        }
//        5. 插入数据到用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建队伍失败");
        }
        return teamId;
    }
}




