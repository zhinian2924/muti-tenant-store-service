package com.example.storesaas.customer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.storesaas.platform.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("biz_customer")
@Data
public class Customer extends BaseEntity {
    private Long tenantId;// 租户ID
    private String openid;// 开放平台ID
    private String nickname;// 昵称
    private String avatarUrl;// 头像
    private Integer status;// 状态
}
