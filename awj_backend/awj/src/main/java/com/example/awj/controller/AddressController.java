package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.AddressDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.AddressService;
import com.example.awj.vo.AddressVo;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    public Result<List<AddressVo>> getAddressList() {
        return Result.success(addressService.getAddressList(getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public Result<AddressVo> getAddressById(@PathVariable Long id) {
        return Result.success(addressService.getAddressById(getCurrentUserId(), id));
    }

    @PostMapping("/create")
    public Result<AddressVo> createAddress(@Valid @RequestBody AddressDto dto) {
        return Result.success(addressService.addAddress(getCurrentUserId(), dto));
    }

    @PostMapping("/update")
    public Result<AddressVo> updateAddress(@Valid @RequestBody AddressDto dto) {
        return Result.success(addressService.updateAddress(getCurrentUserId(), dto));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(getCurrentUserId(), id);
        return Result.success();
    }
}
