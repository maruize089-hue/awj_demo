package com.example.awj.service.impl;

import com.example.awj.dto.BannerDto;
import com.example.awj.entity.Banner;
import com.example.awj.mapper.BannerMapper;
import com.example.awj.service.BannerService;
import com.example.awj.vo.BannerVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    public BannerServiceImpl(BannerMapper bannerMapper) {
        this.bannerMapper = bannerMapper;
    }

    @Override
    public List<BannerVo> getBannerList() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
               .orderByAsc(Banner::getSortOrder);
        
        return bannerMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BannerVo addBanner(BannerDto dto) {
        Banner banner = new Banner();
        banner.setTitle(dto.getTitle());
        banner.setImage(dto.getImage());
        banner.setLink(dto.getLink());
        banner.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        banner.setStatus(1);
        
        bannerMapper.insert(banner);
        return convertToVo(banner);
    }

    @Override
    @Transactional
    public BannerVo updateBanner(BannerDto dto) {
        Banner banner = bannerMapper.selectById(dto.getId());
        if (banner == null) {
            throw new RuntimeException("Banner不存在");
        }
        
        banner.setTitle(dto.getTitle());
        banner.setImage(dto.getImage());
        banner.setLink(dto.getLink());
        banner.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : banner.getSortOrder());
        
        bannerMapper.updateById(banner);
        return convertToVo(banner);
    }

    @Override
    @Transactional
    public void deleteBanner(Long id) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) {
            throw new RuntimeException("Banner不存在");
        }
        bannerMapper.deleteById(id);
    }

    private BannerVo convertToVo(Banner banner) {
        BannerVo vo = new BannerVo();
        vo.setId(banner.getId());
        vo.setTitle(banner.getTitle());
        vo.setImage(banner.getImage());
        vo.setLink(banner.getLink());
        return vo;
    }
}
