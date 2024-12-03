package com.esdllm.usercenter.common;

/**
 * 类名 : ResultUtils
 * 包 : com.esdllm.usercenter.common
 * 描述 :返回工具类
 *
 * @author 饿死的流浪猫<br>
 * <b>创建日期</b><br> 2024/11/29 21:23
 * @version 1.0.1
 */
public class ResultUtils {
    /**
     * 成功
     *
     * @param data 数据
     * @param <T> 泛型
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }
    /**
     * 失败
     *
     * @param errorCode 错误代码
     * @param <T> 泛型
     * @return {@link BaseResponse}<{@link T}>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
}
