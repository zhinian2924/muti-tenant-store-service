package com.example.storesaas.customer.dto;

import jakarta.validation.constraints.*;

/**
 * 地址DTO
 * @param consignee 收货人
 * @param phone 电话
 * @param province 省份
 * @param city 城市
 * @param district 区域
 * @param detail 详细地址
 * @param isDefault 是否默认地址
 */
public record AddressDTO(
        @NotBlank String consignee,
        @NotBlank String phone,
        String province,
        String city,
        String district,
        @NotBlank String detail,
        Boolean isDefault
) {
}
