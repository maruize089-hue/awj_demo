package com.example.awj.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartDto {
    private Long id;
    private Long productId;
    private String type;
    private String name;
    private String spec;
    private String image;
    private BigDecimal price;
    private Integer quantity;
}
