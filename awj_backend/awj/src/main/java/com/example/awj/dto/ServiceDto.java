package com.example.awj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceDto {
    private Long id;
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    @NotBlank(message = "服务名称不能为空")
    private String name;
    private String description;
    private String image;
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price;
    private Integer duration;
}
