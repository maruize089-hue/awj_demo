package com.example.awj.service.impl;

import com.example.awj.dto.RefundDto;
import com.example.awj.entity.ProductOrder;
import com.example.awj.entity.Refund;
import com.example.awj.entity.ServiceOrder;
import com.example.awj.mapper.ProductOrderMapper;
import com.example.awj.mapper.RefundMapper;
import com.example.awj.mapper.ServiceOrderMapper;
import com.example.awj.service.RefundService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundMapper refundMapper;
    private final ProductOrderMapper productOrderMapper;
    private final ServiceOrderMapper serviceOrderMapper;

    public RefundServiceImpl(RefundMapper refundMapper, ProductOrderMapper productOrderMapper,
                           ServiceOrderMapper serviceOrderMapper) {
        this.refundMapper = refundMapper;
        this.productOrderMapper = productOrderMapper;
        this.serviceOrderMapper = serviceOrderMapper;
    }

    @Override
    @Transactional
    public Refund applyRefund(Long userId, RefundDto dto) {
        Long merchantId = null;
        
        if ("PRODUCT".equals(dto.getOrderType())) {
            ProductOrder order = productOrderMapper.selectOne(
                new LambdaQueryWrapper<ProductOrder>().eq(ProductOrder::getOrderNo, dto.getOrderNo()));
            if (order == null || !order.getUserId().equals(userId)) {
                throw new RuntimeException("订单不存在");
            }
            merchantId = order.getMerchantId();
        } else {
            ServiceOrder order = serviceOrderMapper.selectOne(
                new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getOrderNo, dto.getOrderNo()));
            if (order == null || !order.getUserId().equals(userId)) {
                throw new RuntimeException("订单不存在");
            }
            merchantId = order.getMerchantId();
        }
        
        Refund refund = new Refund();
        refund.setOrderNo(dto.getOrderNo());
        refund.setOrderType(dto.getOrderType());
        refund.setUserId(userId);
        refund.setMerchantId(merchantId);
        refund.setAmount(dto.getAmount());
        refund.setReason(dto.getReason());
        refund.setImages(dto.getImages());
        refund.setStatus("APPLYING");
        
        refundMapper.insert(refund);
        return refund;
    }

    @Override
    public List<Refund> getRefundList(Long userId) {
        LambdaQueryWrapper<Refund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Refund::getUserId, userId)
               .orderByDesc(Refund::getCreateTime);
        
        return refundMapper.selectList(wrapper);
    }

    @Override
    public Refund getRefundById(Long userId, Long id) {
        Refund refund = refundMapper.selectById(id);
        if (refund == null || !refund.getUserId().equals(userId)) {
            throw new RuntimeException("退款记录不存在");
        }
        return refund;
    }

    @Override
    @Transactional
    public Refund auditRefund(Long merchantId, Long id, Integer status, String remark) {
        Refund refund = refundMapper.selectById(id);
        if (refund == null || !refund.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("退款记录不存在或无权限");
        }
        if (!"APPLYING".equals(refund.getStatus())) {
            throw new RuntimeException("退款状态不允许审核");
        }
        
        refund.setStatus(status == 1 ? "APPROVED" : "REJECTED");
        refund.setAuditTime(LocalDateTime.now());
        refund.setAuditRemark(remark);
        
        refundMapper.updateById(refund);
        return refund;
    }

    @Override
    @Transactional
    public Refund payRefund(Long merchantId, Long id) {
        Refund refund = refundMapper.selectById(id);
        if (refund == null || !refund.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("退款记录不存在或无权限");
        }
        if (!"APPROVED".equals(refund.getStatus())) {
            throw new RuntimeException("退款状态不允许退款");
        }
        
        refund.setStatus("REFUNDED");
        refund.setRefundTime(LocalDateTime.now());
        
        refundMapper.updateById(refund);
        return refund;
    }
}
