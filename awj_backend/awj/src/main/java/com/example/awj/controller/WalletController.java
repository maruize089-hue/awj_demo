package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.security.LoginUser;
import com.example.awj.service.WalletService;
import com.example.awj.vo.WalletVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    public Result<WalletVo> getWalletInfo() {
        return Result.success(walletService.getWalletInfo(getCurrentUserId()));
    }

    @GetMapping("/records")
    @PreAuthorize("hasRole('USER')")
    public Result<List<Map<String, Object>>> getWalletRecords() {
        return Result.success(walletService.getWalletRecords(getCurrentUserId()));
    }

    @PostMapping("/recharge")
    @PreAuthorize("hasRole('USER')")
    public Result<WalletVo> recharge(@RequestBody RechargeDto dto) {
        return Result.success(walletService.recharge(getCurrentUserId(), dto.getAmount()));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER')")
    public Result<WalletVo> withdraw(@RequestBody WithdrawDto dto) {
        return Result.success(walletService.withdraw(getCurrentUserId(), dto.getAmount()));
    }

    @lombok.Data
    public static class RechargeDto {
        private java.math.BigDecimal amount;
    }

    @lombok.Data
    public static class WithdrawDto {
        private java.math.BigDecimal amount;
    }
}
