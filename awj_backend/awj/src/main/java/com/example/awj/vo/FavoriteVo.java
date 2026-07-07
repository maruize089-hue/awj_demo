package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FavoriteVo {
    private Long id;
    private Long productId;
    private String type;
    private String name;
    private String image;
    private BigDecimal price;
    private LocalDateTime createTime;
}
