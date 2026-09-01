package com.example.storesaas.platform.web;

import com.example.storesaas.platform.error.ResultCode;

/**
 * API响应结果
 * @param code 状态码
 * @param message 提示信息
 * @param data 响应数据
 */
public record ApiResponse<T>(int code, String message, T data) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, "ok", data);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(ResultCode.SUCCESS, "ok", null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }
}
