package com.example.awj.controller.admin;

import com.example.awj.common.result.Result;
import com.example.awj.dto.PageDto;
import com.example.awj.service.MerchantService;
import com.example.awj.vo.MerchantVo;
import com.example.awj.common.result.PageResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/merchants")
public class MerchantManageController {

    private final MerchantService merchantService;

    public MerchantManageController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/list")
    public Result<PageResult<MerchantVo>> getMerchantList(PageDto dto) {
        return Result.success(merchantService.getMerchantList(dto));
    }

    @GetMapping("/{id}")
    public Result<MerchantVo> getMerchantById(@PathVariable Long id) {
        return Result.success(merchantService.getMerchantById(id));
    }

    @PutMapping("/{id}/audit")
    public Result<MerchantVo> auditMerchant(@PathVariable Long id, @RequestParam Integer status, 
                                            @RequestParam(required = false) String remark) {
        return Result.success(merchantService.auditMerchant(id, status, remark));
    }
}
