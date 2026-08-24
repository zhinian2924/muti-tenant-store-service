package com.example.storesaas.identity.auth;

public final class AuthRules {
    // 6位数验证码最小值
    public static final int SMS_CODE_RANGE_MIN = 100000;
    // 6位数验证码最大值
    public static final int SMS_CODE_RANGE_MAX = 1000000;
    // 店铺用户名前缀
    public static final String STORE_USERNAME_PREFIX = "user_";

    private AuthRules() {
    }
}
