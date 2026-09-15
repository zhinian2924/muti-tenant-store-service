package com.example.storesaas.customer.service;

import com.example.storesaas.customer.dto.CustomerProfileUpdateDTO;
import com.example.storesaas.customer.vo.CustomerProfileVO;

public interface CustomerProfileService {
    CustomerProfileVO current();

    CustomerProfileVO update(CustomerProfileUpdateDTO request);
}
