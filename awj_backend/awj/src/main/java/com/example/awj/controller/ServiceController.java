package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.PageDto;
import com.example.awj.dto.ServiceDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.ServiceService;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ServiceVo;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/list")
    public Result<List<ServiceVo>> getServiceList(@RequestParam(required = false) Long categoryId) {
        return Result.success(serviceService.getServiceList(categoryId));
    }

    @GetMapping("/{id}")
    public Result<ServiceVo> getServiceById(@PathVariable Long id) {
        return Result.success(serviceService.getServiceById(id));
    }

    @GetMapping("/search")
    public Result<PageResult<ServiceVo>> searchService(PageDto dto) {
        return Result.success(serviceService.searchService(dto));
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<ServiceVo> addService(@Valid @RequestBody ServiceDto dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(serviceService.addService(loginUser.getId(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<ServiceVo> updateService(@Valid @RequestBody ServiceDto dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(serviceService.updateService(loginUser.getId(), dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<Void> deleteService(@PathVariable Long id) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        serviceService.deleteService(loginUser.getId(), id);
        return Result.success();
    }
}
