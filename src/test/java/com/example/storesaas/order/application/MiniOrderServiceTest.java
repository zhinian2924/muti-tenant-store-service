package com.example.storesaas.order.application;

import com.example.storesaas.order.application.impl.MiniOrderServiceImpl;

import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.customer.mapper.CartItemMapper;
import com.example.storesaas.customer.service.AddressService;
import com.example.storesaas.order.domain.OrderRepository;
import com.example.storesaas.order.domain.OrderStatus;
import com.example.storesaas.order.entity.StoreOrder;
import com.example.storesaas.order.vo.MiniOrderVO;
import com.example.storesaas.platform.error.BusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MiniOrderServiceTest {

    @Test
    void confirmReceiptCompletesPaidOrder() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        StoreOrder order = order(10L, OrderStatus.PAID);
        when(orderRepository.findCustomerOrder(1L, 2L, 10L)).thenReturn(order);

        MiniOrderVO result;
        try (MockedStatic<CustomerContext> context = mockStatic(CustomerContext.class)) {
            context.when(CustomerContext::tenantId).thenReturn(1L);
            context.when(CustomerContext::customerId).thenReturn(2L);
            result = service(orderRepository).confirmReceipt(10L);
        }

        assertEquals(OrderStatus.COMPLETED, result.status());
        assertNotNull(result.updatedAt());
        verify(orderRepository).updateOrder(order);
    }

    @Test
    void confirmReceiptRejectsOrderThatIsNotPaid() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        StoreOrder order = order(10L, OrderStatus.PENDING_PAY);
        when(orderRepository.findCustomerOrder(1L, 2L, 10L)).thenReturn(order);

        BusinessException exception;
        try (MockedStatic<CustomerContext> context = mockStatic(CustomerContext.class)) {
            context.when(CustomerContext::tenantId).thenReturn(1L);
            context.when(CustomerContext::customerId).thenReturn(2L);
            exception = assertThrows(BusinessException.class, () -> service(orderRepository).confirmReceipt(10L));
        }

        assertEquals("当前订单不可确认收货", exception.getMessage());
        verify(orderRepository, never()).updateOrder(order);
    }

    private MiniOrderService service(OrderRepository orderRepository) {
        return new MiniOrderServiceImpl(
                orderRepository,
                mock(OrderPricingService.class),
                mock(AddressService.class),
                mock(CartItemMapper.class));
    }

    private StoreOrder order(Long id, String status) {
        StoreOrder order = new StoreOrder();
        order.setId(id);
        order.setStatus(status);
        return order;
    }
}
