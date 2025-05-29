package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 类名 : UserRegisterRequest
 * 包 : com.esdllm.usercenter.model.request
 * 描述 :用户注册请求体
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/26 20:27
 * @version 1.0.1
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8517359891562240122L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String inspectionCode;
}
