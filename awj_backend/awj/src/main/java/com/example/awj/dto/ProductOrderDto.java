package com.example.awj.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductOrderDto {
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    @NotEmpty(message = "商品列表不能为空")
    private List<OrderItemDto> items;
    
    @Data
    public static class OrderItemDto {
        @NotNull(message = "商品ID不能为空")
        private Long productId;
        private Long skuId;
        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
