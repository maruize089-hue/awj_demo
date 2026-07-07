package com.example.awj.service;

import com.example.awj.controller.OrderController.CreateOrderDto;
import com.example.awj.vo.OrderVo;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderVo createOrder(Long userId, CreateOrderDto dto);
    List<OrderVo> getOrderList(Long userId, String status);
    OrderVo getOrderById(Long userId, Long id);
    Map<String, Integer> getOrderStats(Long userId);
    OrderVo payOrder(Long userId, Long orderId);
    OrderVo completeOrder(Long userId, Long orderId);
    OrderVo cancelOrder(Long userId, Long orderId);
}
