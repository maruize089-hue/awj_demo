package com.example.awj.service.impl;

import com.example.awj.controller.OrderController.CreateOrderDto;
import com.example.awj.controller.OrderController.OrderItemDto;
import com.example.awj.entity.*;
import com.example.awj.mapper.*;
import com.example.awj.service.OrderService;
import com.example.awj.vo.OrderVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;
    private final MerchantMapper merchantMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                           AddressMapper addressMapper, MerchantMapper merchantMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.addressMapper = addressMapper;
        this.merchantMapper = merchantMapper;
    }

    @Override
    @Transactional
    public OrderVo createOrder(Long userId, CreateOrderDto dto) {
        BigDecimal subtotal = BigDecimal.ZERO;
        String orderType = "PRODUCT";
        
        for (OrderItemDto item : dto.getItems()) {
            subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            if ("SERVICE".equals(item.getType())) {
                orderType = "SERVICE";
            }
        }
        
        BigDecimal shippingFee = BigDecimal.ZERO;
        if ("PRODUCT".equals(orderType)) {
            shippingFee = subtotal.compareTo(new BigDecimal("99")) >= 0 ? BigDecimal.ZERO : new BigDecimal("5");
        }
        
        BigDecimal couponAmount = BigDecimal.ZERO;
        if (dto.getCouponId() != null) {
            couponAmount = new BigDecimal("10");
        }
        
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(couponAmount);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            totalAmount = BigDecimal.ZERO;
        }
        
        String orderNo = generateOrderNo();
        
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setOrderType(orderType);
        order.setAddressId(dto.getAddressId());
        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setCouponAmount(couponAmount);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setRemark(dto.getRemark());
        order.setServiceTime(dto.getServiceTime());
        
        orderMapper.insert(order);
        
        for (OrderItemDto item : dto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setType(item.getType());
            orderItem.setName(item.getName());
            orderItem.setImage(item.getImage());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItemMapper.insert(orderItem);
        }
        
        return convertToVo(order);
    }

    @Override
    public List<OrderVo> getOrderList(Long userId, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null && !status.isEmpty() && !"ALL".equals(status)) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        
        return orderMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public OrderVo getOrderById(Long userId, Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        return convertToVo(order);
    }

    @Override
    public Map<String, Integer> getOrderStats(Long userId) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("pending", 0);
        stats.put("paid", 0);
        stats.put("completed", 0);
        stats.put("refunded", 0);
        
        List<OrderMapper.OrderStatusCount> counts = orderMapper.getOrderStats(userId);
        for (OrderMapper.OrderStatusCount count : counts) {
            switch (count.getStatus()) {
                case "PENDING":
                    stats.put("pending", count.getCount());
                    break;
                case "PAID":
                    stats.put("paid", count.getCount());
                    break;
                case "COMPLETED":
                    stats.put("completed", count.getCount());
                    break;
                case "REFUNDED":
                    stats.put("refunded", count.getCount());
                    break;
            }
        }
        
        return stats;
    }

    @Override
    @Transactional
    public OrderVo payOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许支付");
        }
        
        order.setStatus("PAID");
        order.setPayType("WECHAT");
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return convertToVo(order);
    }

    @Override
    @Transactional
    public OrderVo completeOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        if (!"PAID".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许确认完成");
        }
        
        order.setStatus("COMPLETED");
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return convertToVo(order);
    }

    @Override
    @Transactional
    public OrderVo cancelOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setStatus("CANCELLED");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return convertToVo(order);
    }

    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + (int)(Math.random() * 10000);
    }

    private OrderVo convertToVo(Order order) {
        OrderVo vo = new OrderVo();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderType(order.getOrderType());
        vo.setUserId(order.getUserId());
        vo.setAddressId(order.getAddressId());
        vo.setSubtotal(order.getSubtotal());
        vo.setShippingFee(order.getShippingFee());
        vo.setCouponAmount(order.getCouponAmount());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayType(order.getPayType());
        vo.setStatus(order.getStatus());
        vo.setStatusText(getStatusText(order.getStatus()));
        vo.setRemark(order.getRemark());
        vo.setServiceTime(order.getServiceTime());
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());
        vo.setCompleteTime(order.getCompleteTime());
        
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        vo.setItems(items.stream().map(this::convertItemToVo).collect(Collectors.toList()));
        vo.setItemCount(items.size());
        
        Address address = addressMapper.selectById(order.getAddressId());
        if (address != null) {
            OrderVo.AddressVo addrVo = new OrderVo.AddressVo();
            addrVo.setReceiver(address.getReceiverName());
            addrVo.setPhone(address.getReceiverPhone());
            addrVo.setProvince(address.getProvince());
            addrVo.setCity(address.getCity());
            addrVo.setDistrict(address.getDistrict());
            addrVo.setDetail(address.getDetail());
            vo.setAddress(addrVo);
        }
        
        return vo;
    }

    private OrderVo.OrderItemVo convertItemToVo(OrderItem item) {
        OrderVo.OrderItemVo vo = new OrderVo.OrderItemVo();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setType(item.getType());
        vo.setName(item.getName());
        vo.setSpec(item.getSpec());
        vo.setImage(item.getImage());
        vo.setPrice(item.getPrice());
        vo.setQuantity(item.getQuantity());
        return vo;
    }

    private String getStatusText(String status) {
        return switch (status) {
            case "PENDING" -> "待支付";
            case "PAID" -> "待服务";
            case "COMPLETED" -> "已完成";
            case "REFUNDED" -> "已退款";
            case "CANCELLED" -> "已取消";
            default -> status;
        };
    }
}
