package com.esdllm.catFriend.exception;

import com.esdllm.catFriend.common.ErrorCode;

/**
 * 类名 : BusinessException
 * 包 : com.esdllm.usercenter.exception
 * 描述 :自定义异常类
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/12/3 16:04
 * @version 1.0.1
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
