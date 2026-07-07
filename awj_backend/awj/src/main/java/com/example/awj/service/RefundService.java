package com.example.awj.service;

import com.example.awj.dto.RefundDto;
import com.example.awj.entity.Refund;

import java.util.List;

public interface RefundService {
    Refund applyRefund(Long userId, RefundDto dto);
    List<Refund> getRefundList(Long userId);
    Refund getRefundById(Long userId, Long id);
    Refund auditRefund(Long merchantId, Long id, Integer status, String remark);
    Refund payRefund(Long merchantId, Long id);
}
