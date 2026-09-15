package com.example.storesaas.inventory;

import com.example.storesaas.platform.web.PageResult;
import com.example.storesaas.inventory.dto.StockAdjustDTO;
import com.example.storesaas.inventory.entity.InventoryFlow;
import com.example.storesaas.inventory.vo.InventoryFlowVO;

public interface InventoryService {
    InventoryFlowVO adjust(StockAdjustDTO request);

    InventoryFlow createFlow(Long tenantId, Long productId, String flowType, Integer quantity, Integer before, Integer after, String remark);

    PageResult<InventoryFlowVO> flows(int page, int size);
}
