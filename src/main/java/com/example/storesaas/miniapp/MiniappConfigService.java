package com.example.storesaas.miniapp;

import com.example.storesaas.miniapp.dto.MiniappConfigDTO;
import com.example.storesaas.miniapp.vo.MiniappConfigVO;
import com.example.storesaas.tenant.entity.Tenant;

public interface MiniappConfigService {
    MiniappConfigVO get(Long tenantId);

    MiniappConfigVO save(Long tenantId, MiniappConfigDTO request);

    MiniappConfigVO setStatus(Long tenantId, Integer status);

    ActiveMiniapp requireActiveByAppId(String appId);

    Long requireActiveTenantIdByAppId(String appId);

    void requireActiveTenantAccess(Long tenantId);

    record ActiveMiniapp(Long tenantId, String appId, String appSecret, Tenant tenant) {
    }
}
