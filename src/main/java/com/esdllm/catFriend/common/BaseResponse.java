package com.esdllm.catFriend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 类名 : BaseResponse
 * 包 : com.esdllm.usercenter.common
 * 描述 : 通用返回类
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/29 21:15
 * @version 1.0.1
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 2148991111972060167L;

    private int code;

    private String message;

    private T data;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    public BaseResponse(int code, T data ,String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = "";
    }
    public BaseResponse(int code, T data) {
        this(code, data, "");
    }
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
    public BaseResponse(ErrorCode errorCode, T data) {
        this(errorCode.getCode(), data, errorCode.getMessage(), errorCode.getDescription());
    }
}
