package com.esdllm.catFriend.model;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 用户-队伍关系表
 * @TableName user_team
 */
@TableName(value ="user_team")
@Data
public class UserTeam {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 加入时间
     */
    private Date joinTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDelete;
}