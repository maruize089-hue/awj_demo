package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.security.LoginUser;
import com.example.awj.service.OrderService;
import com.example.awj.vo.OrderVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public Result<OrderVo> createOrder(@RequestBody CreateOrderDto dto) {
        return Result.success(orderService.createOrder(getCurrentUserId(), dto));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER', 'MERCHANT')")
    public Result<List<OrderVo>> getOrderList(@RequestParam(required = false) String status) {
        return Result.success(orderService.getOrderList(getCurrentUserId(), status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MERCHANT')")
    public Result<OrderVo> getOrderById(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(getCurrentUserId(), id));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER')")
    public Result<Map<String, Integer>> getOrderStats() {
        return Result.success(orderService.getOrderStats(getCurrentUserId()));
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('USER')")
    public Result<OrderVo> payOrder(@RequestBody PayOrderDto dto) {
        return Result.success(orderService.payOrder(getCurrentUserId(), dto.getOrderId()));
    }

    @PostMapping("/complete")
    @PreAuthorize("hasRole('USER')")
    public Result<OrderVo> completeOrder(@RequestBody CompleteOrderDto dto) {
        return Result.success(orderService.completeOrder(getCurrentUserId(), dto.getOrderId()));
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('USER', 'MERCHANT')")
    public Result<OrderVo> cancelOrder(@RequestBody CancelOrderDto dto) {
        return Result.success(orderService.cancelOrder(getCurrentUserId(), dto.getOrderId()));
    }

    @lombok.Data
    public static class CreateOrderDto {
        private List<OrderItemDto> items;
        private Long addressId;
        private String remark;
        private String serviceTime;
        private Long couponId;
    }

    @lombok.Data
    public static class OrderItemDto {
        private Long productId;
        private String type;
        private String name;
        private java.math.BigDecimal price;
        private String image;
        private Integer quantity;
    }

    @lombok.Data
    public static class PayOrderDto {
        private Long orderId;
    }

    @lombok.Data
    public static class CompleteOrderDto {
        private Long orderId;
    }

    @lombok.Data
    public static class CancelOrderDto {
        private Long orderId;
    }
}
