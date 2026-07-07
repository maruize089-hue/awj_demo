package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceVo {
    private Long id;
    private Long merchantId;
    private String merchantName;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer duration;
    private Integer sales;
    private String status;
    private String detail;
}
