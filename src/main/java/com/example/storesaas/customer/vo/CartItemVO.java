package com.example.storesaas.customer.vo;

import com.example.storesaas.customer.entity.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车项VO
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param deleted
 * @param tenantId 租户ID
 * @param customerId 客户ID
 * @param productId 商品ID
 * @param quantity 购物车项数量
 * @param price 商品价格
 */
public record CartItemVO(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer deleted,
        Long tenantId,
        Long customerId,
        Long productId,
        Integer quantity,
        BigDecimal price
) {
    public static CartItemVO from(CartItem item) {
        return new CartItemVO(
                item.getId(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getDeleted(),
                item.getTenantId(),
                item.getCustomerId(),
                item.getProductId(),
                item.getQuantity(),
                item.getPrice());
    }
}
