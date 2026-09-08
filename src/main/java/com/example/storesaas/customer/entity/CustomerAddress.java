package com.example.storesaas.customer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.storesaas.platform.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户地址
 */
@EqualsAndHashCode(callSuper = true)
@TableName("biz_customer_address")
@Data
public class CustomerAddress extends BaseEntity {
    private Long tenantId;// 租户ID
    private Long customerId;// 客户ID
    private String consignee;// 收货人
    private String phone;// 电话
    private String province;// 省份
    private String city;// 城市
    private String district;// 区域
    private String detail;// 详细地址
    private Integer isDefault;// 是否默认地址
}