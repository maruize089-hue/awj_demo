package com.example.awj.service.impl;

import com.example.awj.dto.MerchantDto;
import com.example.awj.dto.PageDto;
import com.example.awj.entity.Merchant;
import com.example.awj.entity.User;
import com.example.awj.mapper.MerchantMapper;
import com.example.awj.mapper.UserMapper;
import com.example.awj.service.MerchantService;
import com.example.awj.vo.MerchantVo;
import com.example.awj.common.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;
    private final UserMapper userMapper;

    public MerchantServiceImpl(MerchantMapper merchantMapper, UserMapper userMapper) {
        this.merchantMapper = merchantMapper;
        this.userMapper = userMapper;
    }

    @Override
    public MerchantVo getMerchantInfo(Long userId) {
        Merchant merchant = merchantMapper.selectByUserId(userId);
        if (merchant == null) {
            throw new RuntimeException("商家信息不存在");
        }
        return convertToVo(merchant);
    }

    @Override
    @Transactional
    public MerchantVo registerMerchant(Long userId, MerchantDto dto) {
        if (merchantMapper.selectByUserId(userId) != null) {
            throw new RuntimeException("已注册商家");
        }
        
        Merchant merchant = new Merchant();
        merchant.setUserId(userId);
        merchant.setName(dto.getName());
        merchant.setLogo(dto.getLogo());
        merchant.setAddress(dto.getAddress());
        merchant.setPhone(dto.getPhone());
        merchant.setDescription(dto.getDescription());
        merchant.setStatus(0);
        
        merchantMapper.insert(merchant);
        
        User user = userMapper.selectById(userId);
        user.setRole("MERCHANT");
        userMapper.updateById(user);
        
        return convertToVo(merchant);
    }

    @Override
    @Transactional
    public MerchantVo updateMerchant(Long userId, MerchantDto dto) {
        Merchant merchant = merchantMapper.selectByUserId(userId);
        if (merchant == null) {
            throw new RuntimeException("商家信息不存在");
        }
        
        merchant.setName(dto.getName());
        merchant.setLogo(dto.getLogo());
        merchant.setAddress(dto.getAddress());
        merchant.setPhone(dto.getPhone());
        merchant.setDescription(dto.getDescription());
        
        merchantMapper.updateById(merchant);
        return convertToVo(merchant);
    }

    @Override
    public PageResult<MerchantVo> getMerchantList(PageDto dto) {
        Page<Merchant> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(Merchant::getName, dto.getKeyword());
        }
        wrapper.orderByDesc(Merchant::getCreateTime);
        
        Page<Merchant> result = merchantMapper.selectPage(page, wrapper);
        
        return new PageResult<MerchantVo>(result.getTotal(), result.getCurrent(), result.getSize(),
            result.getRecords().stream().map(this::convertToVo).collect(Collectors.toList()));
    }

    @Override
    public MerchantVo getMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
        return convertToVo(merchant);
    }

    @Override
    @Transactional
    public MerchantVo auditMerchant(Long id, Integer status, String remark) {
        Merchant merchant = merchantMapper.selectById(id);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
        
        merchant.setStatus(status);
        merchant.setAuditTime(LocalDateTime.now());
        merchant.setAuditRemark(remark);
        
        merchantMapper.updateById(merchant);
        
        if (status == 1) {
            User user = userMapper.selectById(merchant.getUserId());
            user.setRole("MERCHANT");
            userMapper.updateById(user);
        }
        
        return convertToVo(merchant);
    }

    private MerchantVo convertToVo(Merchant merchant) {
        MerchantVo vo = new MerchantVo();
        vo.setId(merchant.getId());
        vo.setUserId(merchant.getUserId());
        vo.setName(merchant.getName());
        vo.setLogo(merchant.getLogo());
        vo.setAddress(merchant.getAddress());
        vo.setPhone(merchant.getPhone());
        vo.setDescription(merchant.getDescription());
        vo.setStatus(getStatusText(merchant.getStatus()));
        vo.setStatusText(getStatusText(merchant.getStatus()));
        vo.setAuditTime(merchant.getAuditTime());
        vo.setAuditRemark(merchant.getAuditRemark());
        
        User user = userMapper.selectById(merchant.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
        }
        
        return vo;
    }

    private String getStatusText(Integer status) {
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "已通过";
            case 2 -> "已拒绝";
            default -> "未知";
        };
    }
}
