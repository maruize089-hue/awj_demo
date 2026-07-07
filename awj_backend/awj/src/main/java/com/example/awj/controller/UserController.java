package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.AddressDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.AddressService;
import com.example.awj.service.CouponService;
import com.example.awj.service.UserService;
import com.example.awj.vo.AddressVo;
import com.example.awj.vo.CouponVo;
import com.example.awj.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final CouponService couponService;

    public UserController(UserService userService, AddressService addressService, CouponService couponService) {
        this.userService = userService;
        this.addressService = addressService;
        this.couponService = couponService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    public Result<UserVo> getUserInfo() {
        return Result.success(userService.getUserInfo(getCurrentUserId()));
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasRole('USER')")
    public Result<List<AddressVo>> getAddresses() {
        return Result.success(addressService.getAddressList(getCurrentUserId()));
    }

    @PostMapping("/addresses")
    @PreAuthorize("hasRole('USER')")
    public Result<AddressVo> addAddress(@Valid @RequestBody AddressDto dto) {
        return Result.success(addressService.addAddress(getCurrentUserId(), dto));
    }

    @PutMapping("/addresses/{id}")
    @PreAuthorize("hasRole('USER')")
    public Result<AddressVo> updateAddress(@Valid @RequestBody AddressDto dto) {
        return Result.success(addressService.updateAddress(getCurrentUserId(), dto));
    }

    @DeleteMapping("/addresses/{id}")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(getCurrentUserId(), id);
        return Result.success();
    }

    @GetMapping("/coupons")
    @PreAuthorize("hasRole('USER')")
    public Result<List<CouponVo>> getCoupons() {
        return Result.success(couponService.getCouponList(getCurrentUserId()));
    }
}
