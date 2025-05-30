package com.esdllm.catFriend.model.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 队伍状态枚举
 */
public enum TeamStatusEnum {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");
    @Getter
    @Setter
    private int value;
    @Getter
    @Setter
    private String text;

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }
    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TeamStatusEnum teamStatusEnum : TeamStatusEnum.values()) {
            if (teamStatusEnum.value == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }
}
