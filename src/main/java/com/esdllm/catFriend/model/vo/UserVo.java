package com.esdllm.catFriend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户包装类（脱敏）
 */
@Data
public class UserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -979546047587849793L;
    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 昵称
     */
    private String username;

    /**
     * 登录账户
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 校验编号，用于设置后续校验码
     */
    private String inspectionCode;

    /**
     * 个人简介
     */
    private String profile;
    /**
     * 标签列表JSON
     */
    private String tags;
}
