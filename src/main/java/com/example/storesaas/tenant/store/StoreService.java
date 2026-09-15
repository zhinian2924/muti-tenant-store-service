package com.example.storesaas.tenant.store;

import com.example.storesaas.tenant.store.dto.StoreProfileDTO;
import com.example.storesaas.tenant.store.entity.Store;
import com.example.storesaas.tenant.store.vo.PublicStoreVO;
import com.example.storesaas.tenant.store.vo.StoreVO;
import com.example.storesaas.tenant.entity.Tenant;

public interface StoreService {
    StoreVO profile();
    PublicStoreVO publicStore(Long tenantId);
    StoreVO updateProfile(StoreProfileDTO request);
}
