package com.example.storesaas.identity.auth;

import com.example.storesaas.miniapp.infrastructure.WechatClient;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import com.example.storesaas.platform.model.EnableStatus;
import com.example.storesaas.platform.error.ResultCode;
import com.example.storesaas.customer.entity.Customer;
import com.example.storesaas.customer.mapper.CustomerMapper;
import com.example.storesaas.identity.auth.dto.WechatLoginDTO;
import com.example.storesaas.identity.auth.vo.MiniLoginVO;
import com.example.storesaas.miniapp.MiniappConfigService;
import com.example.storesaas.identity.security.AccountType;
import com.example.storesaas.identity.security.LoginUser;
import com.example.storesaas.identity.auth.dto.MockLoginDTO;
import com.example.storesaas.tenant.TenantStatus;
import com.example.storesaas.tenant.entity.Tenant;
import com.example.storesaas.tenant.mapper.TenantMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.security.SecureRandom;

@Service
public class MiniAuthService {
    private static final String NICKNAME_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final CustomerMapper customerMapper;
    private final TenantMapper tenantMapper;
    private final MiniappConfigService configService;
    private final WechatClient wechatClient;

    public MiniAuthService(CustomerMapper customerMapper, TenantMapper tenantMapper,
                           MiniappConfigService configService, WechatClient wechatClient) {
        this.customerMapper = customerMapper;
        this.tenantMapper = tenantMapper;
        this.configService = configService;
        this.wechatClient = wechatClient;
    }

    @Transactional
    public MiniLoginVO wechatLogin(WechatLoginDTO request) {
        var miniapp = configService.requireActiveByAppId(request.appId());
        var wechatSession = wechatClient.exchange(miniapp.appId(), miniapp.appSecret(), request.code());
        Customer customer = findOrCreate(miniapp.tenantId(), wechatSession.openid());
        return createSession(customer);
    }

    @Transactional
    public MiniLoginVO mockLogin(MockLoginDTO request) {
        Tenant tenant = tenantMapper.selectOne(new LambdaQueryWrapper<Tenant>()
                .eq(Tenant::getId, request.tenantId())
                .eq(Tenant::getDeleted, 0));
        if (tenant == null || !Integer.valueOf(TenantStatus.ACTIVE).equals(tenant.getStatus())) {
            throw new BusinessException("门店不存在或未启用");
        }
        return createSession(findOrCreate(request.tenantId(), request.openid()));
    }

    // 根据openId查询或创建消费者
    private Customer findOrCreate(Long tenantId, String openid) {
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getTenantId, tenantId)
                .eq(Customer::getOpenid, openid)
                .eq(Customer::getDeleted, DeleteStatus.NOT_DELETED));
        if (customer == null) {
            customer = new Customer();
            customer.setTenantId(tenantId);
            customer.setOpenid(openid);
            customer.setNickname(randomNickname());
            customer.setStatus(EnableStatus.ENABLED);
            fill(customer);
            customerMapper.insert(customer);
        } else if (!Integer.valueOf(EnableStatus.ENABLED).equals(customer.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN, "消费者账号已停用");
        }
        return customer;
    }

    // 生成随机昵称
    private String randomNickname() {
        StringBuilder suffix = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            suffix.append(NICKNAME_ALPHABET.charAt(RANDOM.nextInt(NICKNAME_ALPHABET.length())));
        }
        return "user_" + suffix;
    }

    // 创建会话
    private MiniLoginVO createSession(Customer c) {
        StpUtil.login("CUSTOMER:" + c.getId());
        StpUtil.getSession().set("loginUser", new LoginUser(c.getId(), c.getTenantId(), AccountType.CUSTOMER,
                c.getOpenid(), null, List.of()));
        SaTokenInfo t = StpUtil.getTokenInfo();
        return new MiniLoginVO(t.getTokenName(), t.getTokenValue(), c.getId(), c.getTenantId(),
                c.getNickname(), c.getAvatarUrl());
    }

    // 填充公共字段
    private void fill(Customer customer) {
        var now = LocalDateTime.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        customer.setDeleted(DeleteStatus.NOT_DELETED);
    }
}
