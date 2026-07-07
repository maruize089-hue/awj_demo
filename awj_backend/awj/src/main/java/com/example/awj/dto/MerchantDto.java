package com.example.awj.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MerchantDto {
    @NotBlank(message = "商家名称不能为空")
    private String name;
    private String logo;
    @NotBlank(message = "地址不能为空")
    private String address;
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    private String description;
}
