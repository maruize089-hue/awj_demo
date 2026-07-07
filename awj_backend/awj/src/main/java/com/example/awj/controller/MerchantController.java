package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.MerchantDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.MerchantService;
import com.example.awj.vo.MerchantVo;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public Result<MerchantVo> registerMerchant(@Valid @RequestBody MerchantDto dto) {
        return Result.success(merchantService.registerMerchant(getCurrentUserId(), dto));
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<MerchantVo> getMerchantInfo() {
        return Result.success(merchantService.getMerchantInfo(getCurrentUserId()));
    }

    @PutMapping("/info")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<MerchantVo> updateMerchant(@Valid @RequestBody MerchantDto dto) {
        return Result.success(merchantService.updateMerchant(getCurrentUserId(), dto));
    }
}
