package com.example.awj.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceOrderDto {
    @NotNull(message = "服务ID不能为空")
    private Long serviceId;
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    private LocalDateTime serviceTime;
}
