package com.example.awj.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("awj_favorite")
public class Favorite {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long productId;
    private String type;
    private String name;
    private String image;
    private BigDecimal price;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
