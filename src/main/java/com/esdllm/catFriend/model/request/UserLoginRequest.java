package com.esdllm.catFriend.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 类名 : UserLoginRequest
 * 包 : com.esdllm.usercenter.model.request
 * 描述 :
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/26 20:47
 * @version 1.0.1
 */
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1781752569899636327L;
    private String userAccount;
    private String userPassword;
}
