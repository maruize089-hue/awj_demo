package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.RefundDto;
import com.example.awj.entity.Refund;
import com.example.awj.security.LoginUser;
import com.example.awj.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refund")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Result<Refund> applyRefund(@Valid @RequestBody RefundDto dto) {
        return Result.success(refundService.applyRefund(getCurrentUserId(), dto));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public Result<List<Refund>> getRefundList() {
        return Result.success(refundService.getRefundList(getCurrentUserId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Result<Refund> getRefundById(@PathVariable Long id) {
        return Result.success(refundService.getRefundById(getCurrentUserId(), id));
    }

    @PutMapping("/{id}/audit")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<Refund> auditRefund(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required = false) String remark) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(refundService.auditRefund(loginUser.getId(), id, status, remark));
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<Refund> payRefund(@PathVariable Long id) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(refundService.payRefund(loginUser.getId(), id));
    }
}
