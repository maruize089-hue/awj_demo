package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("awj_user_coupon")
public class UserCoupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long couponId;
    private String status;
    private LocalDateTime usedTime;
    private String usedOrderNo;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
