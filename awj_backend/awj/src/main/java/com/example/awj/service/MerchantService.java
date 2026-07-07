package com.example.awj.service;

import com.example.awj.dto.MerchantDto;
import com.example.awj.dto.PageDto;
import com.example.awj.vo.MerchantVo;
import com.example.awj.common.result.PageResult;

public interface MerchantService {
    MerchantVo getMerchantInfo(Long userId);
    MerchantVo registerMerchant(Long userId, MerchantDto dto);
    MerchantVo updateMerchant(Long userId, MerchantDto dto);
    PageResult<MerchantVo> getMerchantList(PageDto dto);
    MerchantVo getMerchantById(Long id);
    MerchantVo auditMerchant(Long id, Integer status, String remark);
}
