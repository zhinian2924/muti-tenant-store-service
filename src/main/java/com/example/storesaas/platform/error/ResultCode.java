package com.example.storesaas.platform.error;

public final class ResultCode {
    public static final int SUCCESS = 200;
    // 请求错误
    public static final int BAD_REQUEST = 400;
    // 未授权
    public static final int UNAUTHORIZED = 401;
    // 禁止访问
    public static final int FORBIDDEN = 403;
    // 冲突
    public static final int CONFLICT = 409;
    // 验证错误
    public static final int VALIDATION_ERROR = 422;
    // 内部错误
    public static final int INTERNAL_ERROR = 500;

    private ResultCode() {
    }
}
