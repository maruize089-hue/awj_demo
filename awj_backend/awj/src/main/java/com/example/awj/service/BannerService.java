package com.example.awj.service;

import com.example.awj.dto.BannerDto;
import com.example.awj.vo.BannerVo;

import java.util.List;

public interface BannerService {
    List<BannerVo> getBannerList();
    BannerVo addBanner(BannerDto dto);
    BannerVo updateBanner(BannerDto dto);
    void deleteBanner(Long id);
}
