package com.example.storesaas.customer.service;

import com.example.storesaas.customer.dto.CartItemDTO;
import com.example.storesaas.customer.vo.CartItemVO;

import java.util.List;

public interface CartService {

    List<CartItemVO> list();

    CartItemVO add(Long productId, CartItemDTO request);

    CartItemVO update(Long productId, CartItemDTO request);

    void remove(Long productId);
}
