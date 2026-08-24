package com.example.storesaas.interfaces.admin;

import com.example.storesaas.identity.auth.AuthService;
import com.example.storesaas.identity.auth.dto.LoginDTO;
import com.example.storesaas.identity.auth.vo.LoginVO;
import com.example.storesaas.identity.security.AccountType;
import com.example.storesaas.platform.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/platform")
public class PlatformAuthController {
    private final AuthService authService;

    public PlatformAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginDTO request) {
        return ApiResponse.ok(authService.login(request, AccountType.PLATFORM));
    }
}
