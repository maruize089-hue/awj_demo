package com.example.awj.controller.admin;

import com.example.awj.common.result.Result;
import com.example.awj.dto.BannerDto;
import com.example.awj.service.BannerService;
import com.example.awj.vo.BannerVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banners")
public class BannerManageController {

    private final BannerService bannerService;

    public BannerManageController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping
    public Result<BannerVo> addBanner(@Valid @RequestBody BannerDto dto) {
        return Result.success(bannerService.addBanner(dto));
    }

    @PutMapping("/{id}")
    public Result<BannerVo> updateBanner(@Valid @RequestBody BannerDto dto) {
        return Result.success(bannerService.updateBanner(dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);
        return Result.success();
    }
}
