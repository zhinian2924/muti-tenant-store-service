package com.example.storesaas.order.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.storesaas.customer.CustomerContext;
import com.example.storesaas.customer.entity.CartItem;
import com.example.storesaas.customer.mapper.CartItemMapper;
import com.example.storesaas.customer.service.AddressService;
import com.example.storesaas.customer.vo.AddressVO;
import com.example.storesaas.order.domain.OrderRepository;
import com.example.storesaas.order.domain.OrderStatus;
import com.example.storesaas.order.dto.MiniOrderDTO;
import com.example.storesaas.order.entity.OrderItem;
import com.example.storesaas.order.entity.StoreOrder;
import com.example.storesaas.order.vo.MiniOrderDetailVO;
import com.example.storesaas.order.vo.MiniOrderItemVO;
import com.example.storesaas.order.vo.MiniOrderVO;
import com.example.storesaas.order.vo.OrderPreviewVO;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.persistence.DeleteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MiniOrderService {
    private final OrderRepository orderRepository;
    private final OrderPricingService pricingService;
    private final AddressService addresses;
    private final CartItemMapper cartItemMapper;

    // 预览订单
    public OrderPreviewVO preview(MiniOrderDTO miniOrderDTO) {
        Calculation calculate = calculate(miniOrderDTO);
        BigDecimal deliveryFee = deliveryFee(miniOrderDTO);
        return new OrderPreviewVO(calculate.items.stream().map(MiniOrderItemVO::from).toList(),
                calculate.total, deliveryFee, calculate.total.add(deliveryFee));
    }

    @Transactional
    public MiniOrderVO create(MiniOrderDTO miniOrderDTO) {
        validateCart(miniOrderDTO);
        Calculation calculate = calculate(miniOrderDTO);
        BigDecimal deliveryFee = deliveryFee(miniOrderDTO);
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setTenantId(CustomerContext.tenantId());
        storeOrder.setCustomerId(CustomerContext.customerId());
        storeOrder.setOrderNo("M" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                ThreadLocalRandom.current().nextInt(1000, 9999));
        storeOrder.setStatus(OrderStatus.PENDING_PAY);
        storeOrder.setTotalAmount(calculate.total.add(deliveryFee));
        storeOrder.setDeliveryFee(deliveryFee);
        storeOrder.setFulfillmentType(miniOrderDTO.fulfillmentType());
        storeOrder.setRemark(miniOrderDTO.remark());
        storeOrder.setSource("MINI");
        storeOrder.setAddressSnapshot(snapshot(miniOrderDTO));
        fill(storeOrder);
        orderRepository.saveOrder(storeOrder);
        for (OrderItem i : calculate.items) {
            i.setOrderId(storeOrder.getId());
            fill(i);
            orderRepository.saveItem(i);
        }
        removeSubmittedCartItems(miniOrderDTO);
        return MiniOrderVO.from(storeOrder);
    }

    public List<MiniOrderVO> list() {
        return orderRepository.findTenantOrders(CustomerContext.tenantId()).stream()
                .filter(order -> Objects.equals(order.getCustomerId(), CustomerContext.customerId()))
                .map(MiniOrderVO::from).toList();
    }

    public MiniOrderDetailVO detail(Long id) {
        StoreOrder storeOrder = owned(id);
        List<MiniOrderItemVO> orderItems = orderRepository.findTenantItems(
                CustomerContext.tenantId(), id).stream().map(MiniOrderItemVO::from).toList();
        return new MiniOrderDetailVO(MiniOrderVO.from(storeOrder), orderItems);
    }

    @Transactional
    public MiniOrderVO cancel(Long id) {
        StoreOrder storeOrder = owned(id);
        if (!OrderStatus.PENDING_PAY.equals(storeOrder.getStatus())) throw new BusinessException("当前订单不可取消");
        storeOrder.setStatus(OrderStatus.CANCELLED);
        storeOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.updateOrder(storeOrder);
        return MiniOrderVO.from(storeOrder);
    }

    @Transactional
    public MiniOrderVO confirmReceipt(Long id) {
        StoreOrder storeOrder = owned(id);
        if (!OrderStatus.PAID.equals(storeOrder.getStatus())) throw new BusinessException("当前订单不可确认收货");
        storeOrder.setStatus(OrderStatus.COMPLETED);
        storeOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.updateOrder(storeOrder);
        return MiniOrderVO.from(storeOrder);
    }

    private StoreOrder owned(Long id) {
        StoreOrder storeOrder = orderRepository.findCustomerOrder(CustomerContext.tenantId(), CustomerContext.customerId(), id);
        if (storeOrder == null) throw new BusinessException("订单不存在");
        return storeOrder;
    }

    // 订单计算
    private Calculation calculate(MiniOrderDTO miniOrderDTO) {
        OrderPricingService.PricingResult pricing = pricingService.price(CustomerContext.tenantId(),
                miniOrderDTO.items().stream().map(item -> new OrderPricingService.OrderLine(
                        item.productId(), item.quantity())).toList());
        return new Calculation(pricing.items(), pricing.total());
    }

    // 验证购物车
    private void validateCart(MiniOrderDTO miniOrderDTO) {
        List<Long> productIds = miniOrderDTO.items().stream().map(MiniOrderDTO.Item::productId).distinct().toList();
        long cartItemCount = cartItemMapper.selectCount(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getTenantId, CustomerContext.tenantId())
                .eq(CartItem::getCustomerId, CustomerContext.customerId())
                .eq(CartItem::getDeleted, DeleteStatus.NOT_DELETED)
                .in(CartItem::getProductId, productIds));
        if (cartItemCount != productIds.size()) {
            throw new BusinessException("购物车商品不存在");
        }
    }

    // 移除已提交的购物车商品
    private void removeSubmittedCartItems(MiniOrderDTO miniOrderDTO) {
        List<Long> productIds = miniOrderDTO.items().stream().map(MiniOrderDTO.Item::productId).distinct().toList();
        cartItemMapper.delete(new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getTenantId, CustomerContext.tenantId())
                .eq(CartItem::getCustomerId, CustomerContext.customerId())
                .eq(CartItem::getDeleted, DeleteStatus.NOT_DELETED)
                .in(CartItem::getProductId, productIds));
    }

    private BigDecimal deliveryFee(MiniOrderDTO miniOrderDTO) {
        if ("DELIVERY".equals(miniOrderDTO.fulfillmentType())) {
            if (miniOrderDTO.addressId() == null) throw new BusinessException("配送订单需要地址");
            return BigDecimal.valueOf(5);
        }
        if (!"SELF_PICKUP".equals(miniOrderDTO.fulfillmentType())) throw new BusinessException("履约方式不支持");
        return BigDecimal.ZERO;
    }


    private String snapshot(MiniOrderDTO miniOrderDTO) {
        if (miniOrderDTO.addressId() == null) return null;
        AddressVO addressVO = addresses.list().stream().filter(x -> x.id().equals(miniOrderDTO.addressId())).findFirst()
                .orElseThrow(() -> new BusinessException("地址不存在"));
        return addressVO.consignee() + " " + addressVO.phone() + " " + addressVO.province() + addressVO.city() + addressVO.district() + addressVO.detail();
    }

    private void fill(Object object) {
        LocalDateTime n = LocalDateTime.now();
        if (object instanceof StoreOrder o) {
            o.setCreatedAt(n);
            o.setUpdatedAt(n);
            o.setDeleted(0);
        }
        if (object instanceof OrderItem i) {
            i.setCreatedAt(n);
            i.setUpdatedAt(n);
            i.setDeleted(0);
        }
    }

    private record Calculation(List<OrderItem> items, BigDecimal total) {
    }
}
