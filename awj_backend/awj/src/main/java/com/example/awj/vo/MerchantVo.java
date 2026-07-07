package com.example.awj.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantVo {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String logo;
    private String address;
    private String phone;
    private String description;
    private String status;
    private String statusText;
    private LocalDateTime auditTime;
    private String auditRemark;
}
