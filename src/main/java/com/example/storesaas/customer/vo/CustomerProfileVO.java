package com.example.storesaas.customer.vo;

import com.example.storesaas.customer.entity.Customer;

public record CustomerProfileVO(Long customerId, Long tenantId, String nickname, String avatarUrl) {
    public static CustomerProfileVO from(Customer customer) {
        return new CustomerProfileVO(customer.getId(), customer.getTenantId(),
                customer.getNickname(), customer.getAvatarUrl());
    }
}
