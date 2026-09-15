package com.example.storesaas.order;

import com.example.storesaas.order.dto.CreateOrderDTO;
import com.example.storesaas.order.vo.OrderItemVO;
import com.example.storesaas.order.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderVO create(CreateOrderDTO request);

    List<OrderVO> list();

    List<OrderItemVO> items(Long orderId);
}
