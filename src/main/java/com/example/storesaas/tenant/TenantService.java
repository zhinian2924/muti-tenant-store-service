package com.example.storesaas.tenant;

import com.example.storesaas.tenant.dto.TenantUpdateDTO;
import com.example.storesaas.tenant.entity.Tenant;
import com.example.storesaas.tenant.vo.TenantVO;
import java.util.List;

public interface TenantService {
    List<TenantVO> list(Integer status);
    void update(Long id, TenantUpdateDTO request);
    void setStatus(Long id, Integer status);
    void delete(Long id);
    void approve(Long id);
    void reject(Long id);
}
