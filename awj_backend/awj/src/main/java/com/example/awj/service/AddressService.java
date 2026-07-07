package com.example.awj.service;

import com.example.awj.dto.AddressDto;
import com.example.awj.vo.AddressVo;

import java.util.List;

public interface AddressService {
    List<AddressVo> getAddressList(Long userId);
    AddressVo getAddressById(Long userId, Long id);
    AddressVo addAddress(Long userId, AddressDto dto);
    AddressVo updateAddress(Long userId, AddressDto dto);
    void deleteAddress(Long userId, Long id);
}
