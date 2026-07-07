package com.example.awj.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletVo {
    private Long id;
    private Long userId;
    private BigDecimal balance;
}
