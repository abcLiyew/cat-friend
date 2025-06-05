package com.esdllm.catFriend.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.esdllm.catFriend.common.ErrorCode;
import com.esdllm.catFriend.exception.BusinessException;
import com.esdllm.catFriend.model.Team;
import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.model.UserTeam;
import com.esdllm.catFriend.model.dto.TeamQuery;
import com.esdllm.catFriend.model.enums.TeamStatusEnum;
import com.esdllm.catFriend.model.request.TeamJoinRequest;
import com.esdllm.catFriend.model.request.TeamQuitRequest;
import com.esdllm.catFriend.model.request.TeamUpdateRequest;
import com.esdllm.catFriend.model.vo.TeamUserVo;
import com.esdllm.catFriend.model.vo.UserVo;
import com.esdllm.catFriend.service.TeamService;
import com.esdllm.catFriend.mapper.TeamMapper;
import com.esdllm.catFriend.service.UserService;
import com.esdllm.catFriend.service.UserTeamService;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Resource
    UserService userService;

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

    @Override
    public List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        LambdaQueryWrapper<Team> queryWrapper = new LambdaQueryWrapper<>();
        //组合查询条件
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq(Team::getId, id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)) {
                queryWrapper.in(Team::getId, idList);
            }
            // 模糊匹配标题和描述
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like(Team::getName, searchText).
                        or().like(Team::getDescription, searchText)
                );
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like(Team::getName, name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like(Team::getDescription, description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq(Team::getMaxNum, maxNum);
            }
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq(Team::getUserId, userId);
            }
            //只有公开队伍可以查询,管理员可以查询所有队伍
            Integer status = teamQuery.getStatus();
            if (status == null){
                status = TeamStatusEnum.PUBLIC.getValue();
            }
            if (!isAdmin&&!TeamStatusEnum.PUBLIC.equals(TeamStatusEnum.getEnumByValue(status))) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            if (status > -1) {
                queryWrapper.eq(Team::getStatus, status);
            }
        }
        //不展示已过期的队伍
        //expire_time is null or expire_time >= now()
        queryWrapper.and(
                qw->qw.isNull(Team::getExpireTime).
                        or().ge(Team::getExpireTime,new Date())
        );
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        //关联查询用户信息
        //1.自己写sql
        //查询创建人的信息
        //select * from team t left join user u on t.user_id = u.id
        //TODO 查询已加入队伍的用户信息
        //select * from team t
        //	join user_team ut on t.id = ut.team_id
        //	LEFT JOIN user u ON ut.user_id = u.id
        //2.使用mybatis-plus
        // 关联查询创建人的信息
        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        for (Team team : teamList) {
            Long creatUserId = team.getUserId();
            if (creatUserId == null) {
                continue;
            }
            TeamUserVo teamUserVo = new TeamUserVo();
            try {
                BeanUtils.copyProperties(teamUserVo, team);
            } catch (Exception e) {
                log.error("team Add error", e);
                continue;
            }
            User creatUser = userService.getById(creatUserId);

            if (creatUser != null) {
                UserVo userVo = new UserVo();
                try {
                    BeanUtils.copyProperties(userVo, creatUser);
                } catch (Exception e) {
                    log.error("user Add error", e);
                }
                teamUserVo.setCreatUser(userVo);
            }
            teamUserVoList.add(teamUserVo);
        }
        return teamUserVoList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        Team oldTeam = getTeamById(id);
        if (!Objects.equals(oldTeam.getUserId(), loginUser.getId())&&!userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须设置密码");
            }
        }
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamUpdateRequest);
        } catch (Exception e) {
            log.error("team Update error", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return this.updateById(team);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);
        if (team.getExpireTime()!= null && team.getExpireTime().before(new Date())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,  "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,  "禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)){
            if (StringUtils.isBlank(password)|| !password.equals(team.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,  "密码错误");
            }
        }
        Long userId = loginUser.getId();
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeam::getUserId, userId);
        long hasJoinNumber = userTeamService.count(queryWrapper);
        if (hasJoinNumber > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建和加入5个队伍");
        }
        // 不能重复加入已加入的队伍
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeam::getUserId, userId);
        queryWrapper.eq(UserTeam::getTeamId, teamId);
        long hasUserJoinTeam = userTeamService.count(queryWrapper);
        if (hasUserJoinTeam > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,  "已加入该队伍");
        }
        long teamHasJoinNum = countTeamUserById(teamId);
        if (teamHasJoinNum>=team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,  "队伍已满");
        }
        //插入新的队伍关联信息
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamId);
        userTeam.setUserId(userId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    /**
     * 获取某队伍当前人数
     * @param teamId 队伍id
     * @return 队伍当前人数
     */
    private long countTeamUserById(Long teamId) {
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTeam::getTeamId, teamId);
        return userTeamService.count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        Long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if (count <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,  "未加入队伍");
        }
        long teamHasJoinNum = countTeamUserById(teamId);
        if (teamHasJoinNum <= 1){
            //队伍人数为1，解散
            this.removeById(teamId);
        }else {
            //大于1，退出
            // 判断是否是队长
            if (Objects.equals(team.getUserId(), userId)){
                // 将队长转移给第二个加入队伍的人
                LambdaQueryWrapper<UserTeam> userTeamQueryWrapper = new LambdaQueryWrapper<>();
                userTeamQueryWrapper.eq(UserTeam::getTeamId, teamId);
                userTeamQueryWrapper.last("order by join_time asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextUserTeamLeaderId = nextUserTeam.getUserId();
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextUserTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR,  "更新队长信息失败");
                }
            }
        }
        //  移除该用户与队伍的关联关系
        return userTeamService.remove(queryWrapper);
    }

    /**
     * 根据id获取队伍信息
     * @param teamId 队伍id
     * @return 队伍信息
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,  "队伍不存在");
        }
        return team;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser) {
        Team team = getTeamById(id);
        if (!Objects.equals(team.getUserId(), loginUser.getId())){
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
        long teamId = team.getId();
        queryWrapper.eq(UserTeam::getTeamId, teamId);
        boolean remove = userTeamService.remove(queryWrapper);
        if (!remove){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
        }
        return this.removeById(teamId);
    }
}




