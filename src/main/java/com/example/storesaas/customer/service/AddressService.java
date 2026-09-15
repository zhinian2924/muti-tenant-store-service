package com.example.storesaas.customer.service;

import com.example.storesaas.customer.dto.AddressDTO;
import com.example.storesaas.customer.vo.AddressVO;

import java.util.List;

public interface AddressService {
    List<AddressVO> list();

    AddressVO get(Long id);

    AddressVO create(AddressDTO addressDTO);

    AddressVO update(Long id, AddressDTO addressDTO);

    void remove(Long id);

    AddressVO setDefault(Long id);
}
