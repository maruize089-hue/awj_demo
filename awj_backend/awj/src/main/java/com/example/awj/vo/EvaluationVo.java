package com.example.awj.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EvaluationVo {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private String avatar;
    private Long productId;
    private String productName;
    private Long serviceId;
    private String serviceName;
    private Integer rating;
    private String content;
    private String images;
    private LocalDateTime createTime;
}
