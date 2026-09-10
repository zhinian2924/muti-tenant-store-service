package com.example.storesaas.interfaces.mini;

import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.platform.web.ApiResponse;
import com.example.storesaas.order.dto.MiniOrderDTO;
import com.example.storesaas.order.application.MiniOrderService;
import com.example.storesaas.order.vo.MiniOrderDetailVO;
import com.example.storesaas.order.vo.MiniOrderVO;
import com.example.storesaas.order.vo.OrderPreviewVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mini/orders")
@RequiredArgsConstructor
public class MiniOrderController {
    private final MiniOrderService service;

    @PostMapping("/preview")
    public ApiResponse<OrderPreviewVO> preview(@Valid @RequestBody MiniOrderDTO miniOrderDTO) {
        CustomerContext.current();
        return ApiResponse.ok(service.preview(miniOrderDTO));
    }

    @PostMapping
    public ApiResponse<MiniOrderVO> create(@Valid @RequestBody MiniOrderDTO miniOrderDTO) {
        CustomerContext.current();
        return ApiResponse.ok(service.create(miniOrderDTO));
    }

    @GetMapping
    public ApiResponse<List<MiniOrderVO>> list() {
        CustomerContext.current();
        return ApiResponse.ok(service.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<MiniOrderDetailVO> detail(@PathVariable Long id) {
        CustomerContext.current();
        return ApiResponse.ok(service.detail(id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<MiniOrderVO> cancel(@PathVariable Long id) {
        CustomerContext.current();
        return ApiResponse.ok(service.cancel(id));
    }
}
