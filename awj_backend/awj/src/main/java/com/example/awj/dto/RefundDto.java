package com.example.awj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundDto {
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    @NotBlank(message = "订单类型不能为空")
    private String orderType;
    @NotNull(message = "退款金额不能为空")
    private BigDecimal amount;
    @NotBlank(message = "退款原因不能为空")
    private String reason;
    private String images;
}
