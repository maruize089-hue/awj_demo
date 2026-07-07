package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_product_order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long productId;
    private String type;
    private String name;
    private String spec;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
