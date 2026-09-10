package com.example.storesaas.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.customer.dto.CustomerProfileUpdateDTO;
import com.example.storesaas.customer.entity.Customer;
import com.example.storesaas.customer.mapper.CustomerMapper;
import com.example.storesaas.customer.vo.CustomerProfileVO;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {
    private final CustomerMapper customerMapper;

    public CustomerProfileVO current() {
        return CustomerProfileVO.from(ownedCurrent());
    }

    @Transactional
    public CustomerProfileVO update(CustomerProfileUpdateDTO request) {
        Customer customer = ownedCurrent();
        customer.setNickname(request.nickname().trim());
        customer.setAvatarUrl(normalize(request.avatarUrl()));
        customer.setUpdatedAt(LocalDateTime.now());
        customerMapper.updateById(customer);
        return CustomerProfileVO.from(customer);
    }

    private Customer ownedCurrent() {
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getId, CustomerContext.customerId())
                .eq(Customer::getTenantId, CustomerContext.tenantId())
                .eq(Customer::getDeleted, DeleteStatus.NOT_DELETED));
        if (customer == null) {
            throw new BusinessException("消费者不存在");
        }
        return customer;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
