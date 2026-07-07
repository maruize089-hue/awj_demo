package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.security.LoginUser;
import com.example.awj.service.CouponService;
import com.example.awj.vo.CouponVo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    public Result<List<CouponVo>> getCouponList(@RequestParam(required = false) String status) {
        return Result.success(couponService.getCouponList(getCurrentUserId(), status));
    }

    @PostMapping("/use")
    public Result<CouponVo> useCoupon(@RequestBody UseCouponDto dto) {
        return Result.success(couponService.useCoupon(getCurrentUserId(), dto.getCouponId()));
    }

    @lombok.Data
    public static class UseCouponDto {
        private Long couponId;
    }
}
