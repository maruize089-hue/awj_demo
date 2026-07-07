package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_product_sku")
public class ProductSku {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String skuCode;
    private String specValues;
    private BigDecimal price;
    private Integer stock;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
