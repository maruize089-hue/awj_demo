package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_refund")
public class Refund {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String orderType;
    private Long userId;
    private Long merchantId;
    private BigDecimal amount;
    private String reason;
    private String images;
    private String status;
    private LocalDateTime auditTime;
    private String auditRemark;
    private LocalDateTime refundTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
