package com.esdllm.catFriend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.esdllm.catFriend.common.BaseResponse;
import com.esdllm.catFriend.common.ErrorCode;
import com.esdllm.catFriend.common.ResultUtils;
import com.esdllm.catFriend.exception.BusinessException;
import com.esdllm.catFriend.model.Team;
import com.esdllm.catFriend.model.User;
import com.esdllm.catFriend.model.dto.TeamQuery;
import com.esdllm.catFriend.model.request.TeamAddRequest;
import com.esdllm.catFriend.model.request.TeamJoinRequest;
import com.esdllm.catFriend.model.request.TeamQuitRequest;
import com.esdllm.catFriend.model.request.TeamUpdateRequest;
import com.esdllm.catFriend.model.vo.TeamUserVo;
import com.esdllm.catFriend.service.TeamService;
import com.esdllm.catFriend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
@Slf4j
@RestController
@RequestMapping("/team")
@Tag(name = "队伍接口", description = "队伍接口")
public class TeamController {
    /**
     * 用户服务对象，用于处理用户相关的业务逻辑
     */
    @Resource
    private UserService userService;

    /**
     * 组队服务对象，用于处理团队相关的业务逻辑
     */
    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    @Operation(summary = "创建队伍")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        try {
            BeanUtils.copyProperties(team, teamAddRequest);
        } catch (Exception e) {
            log.error("team Add error", e);
        }
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    @Operation(summary = "解散队伍")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean delete = teamService.deleteTeam(id,loginUser);
        if (!delete) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @Operation(summary = "更新队伍")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean update = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取单个队伍")
    public BaseResponse<Team> getTeamById( long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    @Operation(summary = "获取队伍列表")
    public BaseResponse<List<TeamUserVo>> listTeams(@ParameterObject TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean admin = userService.isAdmin(request);
        List<TeamUserVo> teamList = teamService.listTeams(teamQuery, admin);
        if (CollectionUtils.isEmpty(teamList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamList);

    }

    @GetMapping("/list/page")
    @Operation(summary = "获取分页的队伍列表")
    public BaseResponse<Page<Team>> listTeamsByPage(@ParameterObject TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team= new Team();
        try {
            BeanUtils.copyProperties(team, teamQuery);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long pageNum = teamQuery.getPageNum();
        long pageSize = teamQuery.getPageSize();
        Page<Team> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = teamService.page(page,queryWrapper);
        if (Objects.isNull(teamPage)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(teamPage);
    }
    @PostMapping("/join")
    @Operation(summary = "加入队伍", description = "加入队伍")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }
    @PostMapping("/quit")
    @Operation(summary = "退出队伍", description = "退出队伍")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }
}

