package com.example.storesaas.interfaces.admin;

import cn.dev33.satoken.stp.StpUtil;
import com.example.storesaas.identity.auth.AuthService;
import com.example.storesaas.identity.auth.dto.AccountProfileUpdateDTO;
import com.example.storesaas.identity.auth.vo.AccountProfileVO;
import com.example.storesaas.platform.web.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {
    private final AuthService authService;

    @GetMapping("/me")
    public ApiResponse<AccountProfileVO> me() {
        return ApiResponse.ok(authService.me());
    }

    @PutMapping("/me")
    public ApiResponse<AccountProfileVO> updateMe(@Valid @RequestBody AccountProfileUpdateDTO request) {
        return ApiResponse.ok(authService.updateMe(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        StpUtil.logout();
        return ApiResponse.ok();
    }
}
