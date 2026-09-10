package com.example.storesaas.interfaces.mini;

import com.example.storesaas.platform.web.ApiResponse;
import com.example.storesaas.identity.auth.dto.WechatLoginDTO;
import com.example.storesaas.identity.auth.MiniAuthService;
import com.example.storesaas.identity.auth.vo.MiniLoginVO;
import com.example.storesaas.customer.dto.CustomerProfileUpdateDTO;
import com.example.storesaas.customer.service.CustomerProfileService;
import com.example.storesaas.customer.vo.CustomerProfileVO;
import com.example.storesaas.identity.auth.vo.MiniCustomerVO;
import com.example.storesaas.identity.security.AuthContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.stp.StpUtil;

@RestController
@RequestMapping("/api/mini/auth")
@RequiredArgsConstructor
public class MiniAuthController {
    private final MiniAuthService service;
    private final CustomerProfileService customerProfileService;

    @PostMapping("/wechat-login")
    public ApiResponse<MiniLoginVO> login(@Valid @RequestBody WechatLoginDTO request) {
        return ApiResponse.ok(service.wechatLogin(request));
    }

    @GetMapping("/me")
    public ApiResponse<MiniCustomerVO> me() {
        var user = AuthContext.currentUser();
        return ApiResponse.ok(MiniCustomerVO.from(user, customerProfileService.current()));
    }

    @PutMapping("/profile")
    public ApiResponse<CustomerProfileVO> updateProfile(@Valid @RequestBody CustomerProfileUpdateDTO request) {
        AuthContext.currentUser();
        return ApiResponse.ok(customerProfileService.update(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        StpUtil.logout();
        return ApiResponse.ok();
    }
}
