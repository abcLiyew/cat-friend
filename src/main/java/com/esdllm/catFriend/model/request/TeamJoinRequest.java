package com.esdllm.catFriend.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class TeamJoinRequest {
    /**
     * 用户id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
