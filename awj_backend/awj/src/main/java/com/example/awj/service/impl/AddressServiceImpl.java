package com.example.awj.service.impl;

import com.example.awj.dto.AddressDto;
import com.example.awj.entity.Address;
import com.example.awj.mapper.AddressMapper;
import com.example.awj.service.AddressService;
import com.example.awj.vo.AddressVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Override
    public List<AddressVo> getAddressList(Long userId) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, userId)
               .orderByDesc(Address::getIsDefault)
               .orderByDesc(Address::getCreateTime);
        
        return addressMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public AddressVo getAddressById(Long userId, Long id) {
        Address address = addressMapper.selectById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在");
        }
        return convertToVo(address);
    }

    @Override
    @Transactional
    public AddressVo addAddress(Long userId, AddressDto dto) {
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, userId);
            List<Address> addresses = addressMapper.selectList(wrapper);
            addresses.forEach(a -> {
                a.setIsDefault(0);
                addressMapper.updateById(a);
            });
        }
        
        Address address = new Address();
        address.setUserId(userId);
        address.setReceiverName(dto.getReceiverName());
        address.setReceiverPhone(dto.getReceiverPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        
        addressMapper.insert(address);
        return convertToVo(address);
    }

    @Override
    @Transactional
    public AddressVo updateAddress(Long userId, AddressDto dto) {
        Address address = addressMapper.selectById(dto.getId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在");
        }
        
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Address::getUserId, userId);
            List<Address> addresses = addressMapper.selectList(wrapper);
            addresses.forEach(a -> {
                a.setIsDefault(0);
                addressMapper.updateById(a);
            });
        }
        
        address.setReceiverName(dto.getReceiverName());
        address.setReceiverPhone(dto.getReceiverPhone());
        address.setProvince(dto.getProvince());
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setDetail(dto.getDetail());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        
        addressMapper.updateById(address);
        return convertToVo(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long id) {
        Address address = addressMapper.selectById(id);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在");
        }
        addressMapper.deleteById(id);
    }

    private AddressVo convertToVo(Address address) {
        AddressVo vo = new AddressVo();
        vo.setId(address.getId());
        vo.setReceiverName(address.getReceiverName());
        vo.setReceiverPhone(address.getReceiverPhone());
        vo.setProvince(address.getProvince());
        vo.setCity(address.getCity());
        vo.setDistrict(address.getDistrict());
        vo.setDetail(address.getDetail());
        vo.setFullAddress(String.join("", 
            address.getProvince() != null ? address.getProvince() : "",
            address.getCity() != null ? address.getCity() : "",
            address.getDistrict() != null ? address.getDistrict() : "",
            address.getDetail() != null ? address.getDetail() : ""
        ));
        vo.setIsDefault(address.getIsDefault());
        return vo;
    }
}
