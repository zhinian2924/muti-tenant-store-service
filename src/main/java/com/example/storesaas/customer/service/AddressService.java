package com.example.storesaas.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.customer.dto.AddressDTO;
import com.example.storesaas.customer.entity.CustomerAddress;
import com.example.storesaas.customer.mapper.CustomerAddressMapper;
import com.example.storesaas.customer.vo.AddressVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressService {
    private final CustomerAddressMapper mapper;

    public AddressService(CustomerAddressMapper mapper) {
        this.mapper = mapper;
    }

    public List<AddressVO> list() {
        return mapper.selectList(
                        query().orderByDesc(CustomerAddress::getIsDefault).orderByDesc(CustomerAddress::getId))
                .stream().map(AddressVO::from).toList();
    }

    @Transactional
    public AddressVO create(AddressDTO addressDTO) {
        CustomerAddress customerAddress = new CustomerAddress();
        copy(customerAddress, addressDTO);
        customerAddress.setTenantId(CustomerContext.tenantId());
        customerAddress.setCustomerId(CustomerContext.customerId());
        fill(customerAddress);
        // 设置默认地址
        if (Boolean.TRUE.equals(addressDTO.isDefault()) || list().isEmpty()) makeDefault(customerAddress);
        mapper.insert(customerAddress);
        return AddressVO.from(customerAddress);
    }

    @Transactional
    public AddressVO update(Long id, AddressDTO r) {
        CustomerAddress a = owned(id);
        copy(a, r);
        a.setUpdatedAt(LocalDateTime.now());
        if (Boolean.TRUE.equals(r.isDefault())) makeDefault(a);
        mapper.updateById(a);
        return AddressVO.from(a);
    }

    @Transactional
    public void remove(Long id) {
        CustomerAddress a = owned(id);
        a.setDeleted(DeleteStatus.DELETED);
        a.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(a);
    }

    @Transactional
    public AddressVO setDefault(Long id) {
        CustomerAddress a = owned(id);
        makeDefault(a);
        mapper.updateById(a);
        return AddressVO.from(a);
    }

    private CustomerAddress owned(Long id) {
        CustomerAddress customerAddress = mapper.selectOne(query().eq(CustomerAddress::getId, id));
        if (customerAddress == null) throw new BusinessException("地址不存在");
        return customerAddress;
    }

    private LambdaQueryWrapper<CustomerAddress> query() {
        return new LambdaQueryWrapper<CustomerAddress>()
                .eq(CustomerAddress::getTenantId, CustomerContext.tenantId())
                .eq(CustomerAddress::getCustomerId, CustomerContext.customerId())
                .eq(CustomerAddress::getDeleted, DeleteStatus.NOT_DELETED);
    }

    private void copy(CustomerAddress customerAddress, AddressDTO addressDTO) {
        customerAddress.setConsignee(addressDTO.consignee());
        customerAddress.setPhone(addressDTO.phone());
        customerAddress.setProvince(addressDTO.province());
        customerAddress.setCity(addressDTO.city());
        customerAddress.setDistrict(addressDTO.district());
        customerAddress.setDetail(addressDTO.detail());
        customerAddress.setIsDefault(Boolean.TRUE.equals(addressDTO.isDefault()) ? 1 : 0);
    }

    private void fill(CustomerAddress customerAddress) {
        LocalDateTime now = LocalDateTime.now();
        customerAddress.setCreatedAt(now);
        customerAddress.setUpdatedAt(now);
        customerAddress.setDeleted(DeleteStatus.NOT_DELETED);
    }

    private void makeDefault(CustomerAddress customerAddress) {
        mapper.update(null, new LambdaUpdateWrapper<CustomerAddress>()
                .eq(CustomerAddress::getTenantId, CustomerContext.tenantId())
                .eq(CustomerAddress::getCustomerId, CustomerContext.customerId())
                .eq(CustomerAddress::getDeleted, DeleteStatus.NOT_DELETED)
                .set(CustomerAddress::getIsDefault, 0));
        customerAddress.setIsDefault(1);
    }
}
