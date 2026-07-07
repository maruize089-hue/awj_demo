package com.example.awj.service.impl;

import com.example.awj.dto.PageDto;
import com.example.awj.dto.ServiceDto;
import com.example.awj.entity.Category;
import com.example.awj.entity.CommunityService;
import com.example.awj.entity.Merchant;
import com.example.awj.mapper.CategoryMapper;
import com.example.awj.mapper.MerchantMapper;
import com.example.awj.mapper.ServiceMapper;
import com.example.awj.service.ServiceService;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ServiceVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceMapper serviceMapper;
    private final CategoryMapper categoryMapper;
    private final MerchantMapper merchantMapper;

    public ServiceServiceImpl(ServiceMapper serviceMapper, CategoryMapper categoryMapper, 
                            MerchantMapper merchantMapper) {
        this.serviceMapper = serviceMapper;
        this.categoryMapper = categoryMapper;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public List<ServiceVo> getServiceList(Long categoryId) {
        LambdaQueryWrapper<CommunityService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityService::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(CommunityService::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(CommunityService::getCreateTime);
        
        return serviceMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public ServiceVo getServiceById(Long id) {
        CommunityService service = serviceMapper.selectById(id);
        if (service == null) {
            throw new RuntimeException("服务不存在");
        }
        return convertToVo(service);
    }

    @Override
    public PageResult<ServiceVo> searchService(PageDto dto) {
        Page<CommunityService> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<CommunityService> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommunityService::getStatus, 1);
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(CommunityService::getName, dto.getKeyword());
        }
        wrapper.orderByDesc(CommunityService::getCreateTime);
        
        Page<CommunityService> result = serviceMapper.selectPage(page, wrapper);
        
        List<ServiceVo> records = result.getRecords().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
        
        return new PageResult<ServiceVo>(result.getTotal(), result.getCurrent(), result.getSize(), records);
    }

    @Override
    @Transactional
    public ServiceVo addService(Long merchantId, ServiceDto dto) {
        CommunityService service = new CommunityService();
        service.setMerchantId(merchantId);
        service.setCategoryId(dto.getCategoryId());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setImage(dto.getImage());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        service.setStatus(0);
        
        serviceMapper.insert(service);
        return convertToVo(service);
    }

    @Override
    @Transactional
    public ServiceVo updateService(Long merchantId, ServiceDto dto) {
        CommunityService service = serviceMapper.selectById(dto.getId());
        if (service == null || !service.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("服务不存在或无权限");
        }
        
        service.setCategoryId(dto.getCategoryId());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setImage(dto.getImage());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        
        serviceMapper.updateById(service);
        return convertToVo(service);
    }

    @Override
    @Transactional
    public void deleteService(Long merchantId, Long id) {
        CommunityService service = serviceMapper.selectById(id);
        if (service == null || !service.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("服务不存在或无权限");
        }
        serviceMapper.deleteById(id);
    }

    private ServiceVo convertToVo(CommunityService service) {
        ServiceVo vo = new ServiceVo();
        vo.setId(service.getId());
        vo.setMerchantId(service.getMerchantId());
        vo.setCategoryId(service.getCategoryId());
        vo.setName(service.getName());
        vo.setDescription(service.getDescription());
        vo.setImage(service.getImage());
        vo.setPrice(service.getPrice());
        vo.setDuration(service.getDuration());
        vo.setSales(service.getSales());
        vo.setStatus(service.getStatus().equals(1) ? "上架" : "下架");
        
        Merchant merchant = merchantMapper.selectById(service.getMerchantId());
        if (merchant != null) {
            vo.setMerchantName(merchant.getName());
        }
        
        Category category = categoryMapper.selectById(service.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        
        return vo;
    }
}
