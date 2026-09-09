package com.example.storesaas.identity.auth.vo;

import com.example.storesaas.identity.security.AccountType;
import com.example.storesaas.identity.security.LoginUser;
import com.example.storesaas.customer.vo.CustomerProfileVO;

import java.util.List;

/**
 * @param userId 用户ID
 * @param tenantId 租户ID
 * @param accountType 账号类型
 * @param username 用户名
 * @param staffRole 员工角色
 * @param permissions 权限列表
 * @param nickname 昵称
 * @param avatarUrl 头像地址
 */
public record MiniCustomerVO(
        Long userId,
        Long tenantId,
        AccountType accountType,
        String username,
        String staffRole,
        List<String> permissions,
        String nickname,
        String avatarUrl) {

    public static MiniCustomerVO from(LoginUser user) {
        return new MiniCustomerVO(
                user.userId(),
                user.tenantId(),
                user.accountType(),
                user.username(),
                user.staffRole(),
                user.permissions(),
                null,
                null);
    }

    public static MiniCustomerVO from(LoginUser user, CustomerProfileVO profile) {
        return new MiniCustomerVO(user.userId(), user.tenantId(), user.accountType(),
                user.username(), user.staffRole(), user.permissions(),
                profile.nickname(), profile.avatarUrl());
    }
}
