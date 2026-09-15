package com.example.storesaas.order.application;

import com.example.storesaas.order.dto.MiniOrderDTO;
import com.example.storesaas.order.vo.MiniOrderDetailVO;
import com.example.storesaas.order.vo.MiniOrderVO;
import com.example.storesaas.order.vo.OrderPreviewVO;
import java.util.List;

public interface MiniOrderService {
    OrderPreviewVO preview(MiniOrderDTO miniOrderDTO);
    MiniOrderVO create(MiniOrderDTO miniOrderDTO);
    List<MiniOrderVO> list();
    MiniOrderDetailVO detail(Long id);
    MiniOrderVO cancel(Long id);
    MiniOrderVO confirmReceipt(Long id);
}
