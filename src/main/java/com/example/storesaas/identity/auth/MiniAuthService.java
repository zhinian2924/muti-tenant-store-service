package com.example.storesaas.identity.auth;

import com.example.storesaas.identity.auth.dto.WechatLoginDTO;
import com.example.storesaas.identity.auth.vo.MiniLoginVO;
import com.example.storesaas.identity.auth.dto.MockLoginDTO;

public interface MiniAuthService {
    MiniLoginVO wechatLogin(WechatLoginDTO request);

    MiniLoginVO mockLogin(MockLoginDTO request);
}
