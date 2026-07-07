package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_service_order")
public class ServiceOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long merchantId;
    private Long serviceId;
    private Long addressId;
    private BigDecimal totalAmount;
    private LocalDateTime serviceTime;
    private String status;
    private LocalDateTime payTime;
    private LocalDateTime confirmTime;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private LocalDateTime cancelTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
