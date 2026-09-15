package com.example.storesaas.order.application;

import com.example.storesaas.order.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public interface OrderPricingService {
    PricingResult price(Long tenantId, List<OrderLine> lines);

    record OrderLine(Long productId, Integer quantity) {
    }

    record PricingResult(List<OrderItem> items, BigDecimal total) {
    }
}
