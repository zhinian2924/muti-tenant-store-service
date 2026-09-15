package com.example.storesaas.payment;

import com.example.storesaas.order.entity.StoreOrder;

public interface PaymentService {
    StoreOrder mockPay(Long orderId);
}
