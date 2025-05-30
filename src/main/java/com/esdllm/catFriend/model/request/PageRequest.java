package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1180531665576473439L;
    /**
     * 页面大小
     */
    protected int pageSize = 10;
    /**
     * 当前页号
     */
    protected int pageNum = 1;

}
