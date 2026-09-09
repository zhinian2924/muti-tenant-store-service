package com.example.storesaas.customer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.customer.dto.CartItemDTO;
import com.example.storesaas.customer.entity.CartItem;
import com.example.storesaas.customer.mapper.CartItemMapper;
import com.example.storesaas.customer.vo.CartItemVO;
import com.example.storesaas.catalog.application.ProductService;
import com.example.storesaas.catalog.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {
    private final CartItemMapper mapper;
    private final ProductService products;

    public CartService(CartItemMapper mapper, ProductService products) {
        this.mapper = mapper;
        this.products = products;
    }

    public List<CartItemVO> list() {
        Long tenantId = CustomerContext.tenantId();
        return mapper.selectList(query()).stream()
                .map(item -> CartItemVO.from(item, products.tenantProduct(tenantId, item.getProductId())))
                .toList();
    }

    @Transactional
    public CartItemVO add(Long productId, CartItemDTO request) {
        Long tenantId = CustomerContext.tenantId();
        Product product = products.tenantProduct(tenantId, productId);
        if (product.getStatus() == null || product.getStatus() != 1) throw new BusinessException("商品当前不可购买");
        CartItem item = new CartItem();
        item.setTenantId(tenantId);
        item.setCustomerId(CustomerContext.customerId());
        item.setProductId(productId);
        item.setQuantity(request.quantity());
        item.setPrice(product.getPrice());
        fill(item);
        mapper.upsert(item);
        item = mapper.selectOne(query().eq(CartItem::getProductId, productId));
        return CartItemVO.from(item, product);
    }

    @Transactional
    public CartItemVO update(Long productId, CartItemDTO request) {
        CartItem item = owned(productId);
        Product product = products.tenantProduct(CustomerContext.tenantId(), productId);
        item.setQuantity(request.quantity());
        item.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(item);
        return CartItemVO.from(item, product);
    }

    @Transactional
    public void remove(Long productId) {
        CartItem item = owned(productId);
        mapper.deleteById(item.getId());
    }

    // 查询购物车商品
    private CartItem owned(Long productId) {
        CartItem item = mapper.selectOne(query().eq(CartItem::getProductId, productId));
        if (item == null) throw new BusinessException("购物车商品不存在");
        return item;
    }

    private LambdaQueryWrapper<CartItem> query() {
        return new LambdaQueryWrapper<CartItem>().eq(CartItem::getTenantId,
                CustomerContext.tenantId()).eq(CartItem::getCustomerId,
                CustomerContext.customerId()).eq(CartItem::getDeleted, DeleteStatus.NOT_DELETED);
    }

    // 填充商品信息
    private void fill(CartItem item) {
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        item.setDeleted(DeleteStatus.NOT_DELETED);
    }
}
