package com.example.awj.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVo {
    private Long id;
    private String title;
    private String content;
    private String type;
    private String typeText;
    private Integer isRead;
    private LocalDateTime createTime;
}
