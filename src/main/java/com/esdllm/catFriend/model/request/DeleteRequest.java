package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用的删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4982488233676349462L;

    private Long id;
}
