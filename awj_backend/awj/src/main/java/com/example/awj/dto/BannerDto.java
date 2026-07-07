package com.example.awj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BannerDto {
    private Long id;
    private String title;
    @NotBlank(message = "图片地址不能为空")
    private String image;
    private String link;
    private Integer sortOrder;
}
