package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVo {
    private Long id;
    private Long productId;
    private String type;
    private String name;
    private String spec;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    private Integer selected;
}
