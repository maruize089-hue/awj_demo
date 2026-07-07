package com.example.awj.service.impl;

import com.example.awj.entity.Coupon;
import com.example.awj.entity.UserCoupon;
import com.example.awj.mapper.CouponMapper;
import com.example.awj.mapper.UserCouponMapper;
import com.example.awj.service.CouponService;
import com.example.awj.vo.CouponVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    public CouponServiceImpl(CouponMapper couponMapper, UserCouponMapper userCouponMapper) {
        this.couponMapper = couponMapper;
        this.userCouponMapper = userCouponMapper;
    }

    @Override
    public List<CouponVo> getCouponList(Long userId) {
        return getCouponList(userId, null);
    }

    @Override
    public List<CouponVo> getCouponList(Long userId, String status) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        
        List<UserCoupon> userCoupons = userCouponMapper.selectList(wrapper);
        
        return userCoupons.stream()
            .map(uc -> {
                Coupon coupon = couponMapper.selectById(uc.getCouponId());
                return convertToVo(coupon, uc);
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponVo useCoupon(Long userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId)
               .eq(UserCoupon::getCouponId, couponId)
               .eq(UserCoupon::getStatus, "AVAILABLE");
        
        UserCoupon userCoupon = userCouponMapper.selectOne(wrapper);
        if (userCoupon == null) {
            throw new RuntimeException("优惠券不存在或已使用");
        }
        
        userCoupon.setStatus("USED");
        userCoupon.setUsedTime(LocalDateTime.now());
        userCouponMapper.updateById(userCoupon);
        
        Coupon coupon = couponMapper.selectById(couponId);
        return convertToVo(coupon, userCoupon);
    }

    private CouponVo convertToVo(Coupon coupon, UserCoupon userCoupon) {
        CouponVo vo = new CouponVo();
        vo.setId(coupon.getId());
        vo.setName(coupon.getName());
        vo.setAmount(coupon.getAmount());
        vo.setMinAmount(coupon.getMinAmount());
        vo.setStartTime(coupon.getStartTime());
        vo.setEndTime(coupon.getEndTime());
        vo.setStatus(userCoupon != null ? userCoupon.getStatus() : coupon.getStatus());
        return vo;
    }
}
