package com.example.storesaas.payment.application.impl;

import com.example.storesaas.payment.application.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.order.entity.StoreOrder;
import com.example.storesaas.order.mapper.StoreOrderMapper;
import com.example.storesaas.order.vo.MiniOrderVO;
import com.example.storesaas.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MiniPaymentServiceImpl implements MiniPaymentService {
    private final StoreOrderMapper orders;
    private final PaymentService payments;

    public MiniOrderVO mock(Long id) {
        StoreOrder o = orders.selectOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getTenantId, CustomerContext.tenantId()).eq(StoreOrder::getCustomerId, CustomerContext.customerId()).eq(StoreOrder::getId, id).eq(StoreOrder::getDeleted, DeleteStatus.NOT_DELETED));
        if (o == null) throw new BusinessException("订单不存在");
        return MiniOrderVO.from(payments.mockPay(id));
    }
}
