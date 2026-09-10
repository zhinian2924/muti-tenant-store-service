package com.example.storesaas.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * 小程序订单DTO
 * @param items 订单项列表
 * @param fulfillmentType 满足条件
 * @param addressId 地址ID
 * @param remark 备注
 */
public record MiniOrderDTO(
        @NotEmpty List<@Valid Item> items,
        String fulfillmentType,
        Long addressId,
        String remark)
{
    public MiniOrderDTO {
        if (fulfillmentType == null || fulfillmentType.isBlank()) {
            fulfillmentType = "SELF_PICKUP";
        }
    }

    public record Item(
            @NotNull Long productId,
            @NotNull @Min(1) Integer quantity) {
    }
}
