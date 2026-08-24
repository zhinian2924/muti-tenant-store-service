package com.example.storesaas.interfaces.admin;

import com.example.storesaas.identity.auth.AuthService;
import com.example.storesaas.identity.auth.dto.RegisterTenantDTO;
import com.example.storesaas.platform.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/tenant")
public class TenantRegistrationController {
    private final AuthService authService;

    public TenantRegistrationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterTenantDTO request) {
        authService.registerTenant(request);
        return ApiResponse.ok();
    }
}
