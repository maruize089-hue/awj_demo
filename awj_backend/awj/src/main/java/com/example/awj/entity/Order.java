package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_product_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private String orderType;
    private Long addressId;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal couponAmount;
    private BigDecimal totalAmount;
    private String payType;
    private String status;
    private String remark;
    private String serviceTime;
    private LocalDateTime payTime;
    private LocalDateTime completeTime;
    private LocalDateTime cancelTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
