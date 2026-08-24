package com.example.storesaas.identity.auth.vo;

/**
 * 短信验证码VO
 * @param mobile 手机号
 * @param expireSeconds 过期时间（秒）
 * @param debugCode 调试码
 */
public record SmsCodeVO(
        String mobile,
        Integer expireSeconds,
        String debugCode
) {
}
