package com.example.awj.service;

import com.example.awj.vo.CouponVo;

import java.util.List;

public interface CouponService {
    List<CouponVo> getCouponList(Long userId);
    List<CouponVo> getCouponList(Long userId, String status);
    CouponVo useCoupon(Long userId, Long couponId);
}
