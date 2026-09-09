package com.example.storesaas.customer.vo;

import com.example.storesaas.customer.entity.CartItem;
import com.example.storesaas.catalog.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @param id
 * @param createdAt
 * @param updatedAt
 * @param deleted
 * @param tenantId
 * @param customerId
 * @param productId
 * @param productName
 * @param imageUrl
 * @param quantity
 * @param price
 */
public record CartItemVO(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer deleted,
        Long tenantId,
        Long customerId,
        Long productId,
        String productName,
        String imageUrl,
        Integer quantity,
        BigDecimal price
) {
    public static CartItemVO from(CartItem item) {
        return from(item, null);
    }

    public static CartItemVO from(CartItem item, Product product) {
        return new CartItemVO(
                item.getId(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getDeleted(),
                item.getTenantId(),
                item.getCustomerId(),
                item.getProductId(),
                product == null ? null : product.getName(),
                product == null ? null : product.getImageUrl(),
                item.getQuantity(),
                item.getPrice());
    }
}
