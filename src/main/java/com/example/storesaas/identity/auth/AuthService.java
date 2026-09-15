package com.example.storesaas.identity.auth;

import com.example.storesaas.identity.auth.vo.AccountProfileVO;
import com.example.storesaas.identity.auth.dto.AccountProfileUpdateDTO;
import com.example.storesaas.identity.auth.dto.LoginDTO;
import com.example.storesaas.identity.auth.vo.LoginVO;
import com.example.storesaas.identity.auth.dto.RegisterTenantDTO;
import com.example.storesaas.identity.auth.dto.SmsCodeDTO;
import com.example.storesaas.identity.auth.vo.SmsCodeVO;
import com.example.storesaas.identity.security.AccountType;

public interface AuthService {
    void registerTenant(RegisterTenantDTO request);

    SmsCodeVO sendStoreSmsCode(SmsCodeDTO request);

    LoginVO login(LoginDTO request, AccountType accountType);

    AccountProfileVO me();

    AccountProfileVO updateMe(AccountProfileUpdateDTO request);
}
