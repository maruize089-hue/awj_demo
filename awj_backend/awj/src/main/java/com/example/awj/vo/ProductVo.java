package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductVo {
    private Long id;
    private Long merchantId;
    private String merchantName;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private String image;
    private Integer totalStock;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer sales;
    private String status;
    private String detail;
    private List<SpecVo> specs;
    
    @Data
    public static class SpecVo {
        private Long id;
        private String name;
        private BigDecimal price;
    }
}
