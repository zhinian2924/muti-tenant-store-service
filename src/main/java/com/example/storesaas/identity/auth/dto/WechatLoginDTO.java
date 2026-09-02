package com.example.storesaas.identity.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 微信登录传输对象
 * @param code 微信登录凭证
 * @param appId 小程序AppId
 */
public record WechatLoginDTO(
        @NotBlank(message = "缺少微信登录凭证") String code,
        @NotBlank(message = "缺少小程序AppID") @Pattern(regexp = "^wx[a-zA-Z0-9]{16}$", message = "小程序AppID格式不正确") String appId
) {
}
