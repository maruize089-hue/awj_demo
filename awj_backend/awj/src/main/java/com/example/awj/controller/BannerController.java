package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.service.BannerService;
import com.example.awj.vo.BannerVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/list")
    public Result<List<BannerVo>> getBannerList() {
        return Result.success(bannerService.getBannerList());
    }
}
