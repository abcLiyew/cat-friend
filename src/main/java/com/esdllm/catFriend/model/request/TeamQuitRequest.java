package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = -4374977639229211890L;

    private Long teamId;
}
