package com.example.awj.service;

import com.example.awj.dto.PageDto;
import com.example.awj.dto.ServiceDto;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ServiceVo;

import java.util.List;

public interface ServiceService {
    List<ServiceVo> getServiceList(Long categoryId);
    ServiceVo getServiceById(Long id);
    PageResult<ServiceVo> searchService(PageDto dto);
    ServiceVo addService(Long merchantId, ServiceDto dto);
    ServiceVo updateService(Long merchantId, ServiceDto dto);
    void deleteService(Long merchantId, Long id);
}
