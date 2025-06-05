package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6959665397313893819L;
    /**
     * 用户id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
