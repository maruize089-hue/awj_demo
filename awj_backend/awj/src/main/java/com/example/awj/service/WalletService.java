package com.example.awj.service;

import com.example.awj.vo.WalletVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletService {
    WalletVo getWalletInfo(Long userId);
    List<Map<String, Object>> getWalletRecords(Long userId);
    WalletVo recharge(Long userId, BigDecimal amount);
    WalletVo withdraw(Long userId, BigDecimal amount);
}
