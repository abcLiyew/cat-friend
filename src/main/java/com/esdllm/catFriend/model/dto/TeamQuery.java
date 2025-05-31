package com.esdllm.catFriend.model.dto;

import com.esdllm.catFriend.model.request.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 队伍查询封装
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 搜索词，同时匹配队伍名称和描述
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 创建用户的id
     */
    private Long userId;

    /**
     * -  0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;


}
