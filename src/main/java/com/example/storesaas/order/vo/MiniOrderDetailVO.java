package com.example.storesaas.order.vo;

import java.util.List;

/**
 * 小程序订单详情VO
 * @param order
 * @param items
 */
public record MiniOrderDetailVO(
        MiniOrderVO order,
        List<MiniOrderItemVO> items) {
}
