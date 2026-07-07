package com.example.awj.service.impl;

import com.example.awj.dto.EvaluationDto;
import com.example.awj.dto.PageDto;
import com.example.awj.entity.*;
import com.example.awj.mapper.*;
import com.example.awj.service.EvaluationService;
import com.example.awj.vo.EvaluationVo;
import com.example.awj.common.result.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationMapper evaluationMapper;
    private final ProductOrderMapper productOrderMapper;
    private final ServiceOrderMapper serviceOrderMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final ServiceMapper serviceMapper;

    public EvaluationServiceImpl(EvaluationMapper evaluationMapper, ProductOrderMapper productOrderMapper,
                                ServiceOrderMapper serviceOrderMapper, UserMapper userMapper,
                                ProductMapper productMapper, ServiceMapper serviceMapper) {
        this.evaluationMapper = evaluationMapper;
        this.productOrderMapper = productOrderMapper;
        this.serviceOrderMapper = serviceOrderMapper;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
        this.serviceMapper = serviceMapper;
    }

    @Override
    @Transactional
    public Evaluation createEvaluation(Long userId, EvaluationDto dto) {
        Long merchantId = null;
        Long productId = null;
        Long serviceId = null;
        
        if ("PRODUCT".equals(dto.getOrderType())) {
            ProductOrder order = productOrderMapper.selectOne(
                new LambdaQueryWrapper<ProductOrder>().eq(ProductOrder::getOrderNo, dto.getOrderNo()));
            if (order == null || !order.getUserId().equals(userId)) {
                throw new RuntimeException("订单不存在");
            }
            merchantId = order.getMerchantId();
            productId = order.getId();
        } else {
            ServiceOrder order = serviceOrderMapper.selectOne(
                new LambdaQueryWrapper<ServiceOrder>().eq(ServiceOrder::getOrderNo, dto.getOrderNo()));
            if (order == null || !order.getUserId().equals(userId)) {
                throw new RuntimeException("订单不存在");
            }
            merchantId = order.getMerchantId();
            serviceId = order.getServiceId();
        }
        
        Evaluation evaluation = new Evaluation();
        evaluation.setOrderNo(dto.getOrderNo());
        evaluation.setOrderType(dto.getOrderType());
        evaluation.setUserId(userId);
        evaluation.setMerchantId(merchantId);
        evaluation.setProductId(productId);
        evaluation.setServiceId(serviceId);
        evaluation.setRating(dto.getRating());
        evaluation.setContent(dto.getContent());
        evaluation.setImages(dto.getImages());
        evaluation.setStatus(1);
        
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    @Override
    @Transactional
    public EvaluationVo addEvaluation(Long userId, EvaluationDto dto) {
        Evaluation evaluation = new Evaluation();
        evaluation.setUserId(userId);
        evaluation.setOrderId(dto.getOrderId());
        evaluation.setProductId(dto.getProductId());
        evaluation.setRating(dto.getRating());
        evaluation.setContent(dto.getContent());
        evaluation.setImages(dto.getImages());
        evaluation.setStatus(1);
        
        evaluationMapper.insert(evaluation);
        return convertToVo(evaluation);
    }

    @Override
    public PageResult<EvaluationVo> getEvaluationList(PageDto dto) {
        Page<Evaluation> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<Evaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Evaluation::getStatus, 1)
               .orderByDesc(Evaluation::getCreateTime);
        
        Page<Evaluation> result = evaluationMapper.selectPage(page, wrapper);
        
        return new PageResult<EvaluationVo>(result.getTotal(), result.getCurrent(), result.getSize(),
            result.getRecords().stream().map(this::convertToVo).collect(Collectors.toList()));
    }

    @Override
    public List<EvaluationVo> getEvaluationList(Long productId, String type) {
        LambdaQueryWrapper<Evaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Evaluation::getStatus, 1);
        if (productId != null) {
            wrapper.eq(Evaluation::getProductId, productId);
        }
        wrapper.orderByDesc(Evaluation::getCreateTime);
        
        return evaluationMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasEvaluation(Long userId, Long orderId, Long productId) {
        LambdaQueryWrapper<Evaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Evaluation::getUserId, userId);
        if (orderId != null) {
            wrapper.eq(Evaluation::getOrderId, orderId);
        }
        if (productId != null) {
            wrapper.eq(Evaluation::getProductId, productId);
        }
        
        return evaluationMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = evaluationMapper.selectById(id);
        if (evaluation == null) {
            throw new RuntimeException("评价不存在");
        }
        evaluationMapper.deleteById(id);
    }

    private EvaluationVo convertToVo(Evaluation evaluation) {
        EvaluationVo vo = new EvaluationVo();
        vo.setId(evaluation.getId());
        vo.setOrderNo(evaluation.getOrderNo());
        vo.setUserId(evaluation.getUserId());
        vo.setProductId(evaluation.getProductId());
        vo.setServiceId(evaluation.getServiceId());
        vo.setRating(evaluation.getRating());
        vo.setContent(evaluation.getContent());
        vo.setImages(evaluation.getImages());
        vo.setCreateTime(evaluation.getCreateTime());
        
        User user = userMapper.selectById(evaluation.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setAvatar(user.getAvatar());
        }
        
        if (evaluation.getProductId() != null) {
            Product product = productMapper.selectById(evaluation.getProductId());
            if (product != null) {
                vo.setProductName(product.getName());
            }
        }
        
        if (evaluation.getServiceId() != null) {
            CommunityService service = serviceMapper.selectById(evaluation.getServiceId());
            if (service != null) {
                vo.setServiceName(service.getName());
            }
        }
        
        return vo;
    }
}
