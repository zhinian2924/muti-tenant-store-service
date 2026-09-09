package com.example.storesaas.customer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.storesaas.platform.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@TableName("biz_cart_item")
@Data
public class CartItem extends BaseEntity {
    // 租户ID
    private Long tenantId;
    // 客户ID
    private Long customerId;
    // 商品ID
    private Long productId;
    // 数量
    private Integer quantity;
    // 价格
    private BigDecimal price;
}
