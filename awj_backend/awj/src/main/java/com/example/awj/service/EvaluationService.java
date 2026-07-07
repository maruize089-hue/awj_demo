package com.example.awj.service;

import com.example.awj.dto.EvaluationDto;
import com.example.awj.dto.PageDto;
import com.example.awj.vo.EvaluationVo;
import com.example.awj.common.result.PageResult;

import java.util.List;

public interface EvaluationService {
    EvaluationVo addEvaluation(Long userId, EvaluationDto dto);
    PageResult<EvaluationVo> getEvaluationList(PageDto dto);
    List<EvaluationVo> getEvaluationList(Long productId, String type);
    boolean hasEvaluation(Long userId, Long orderId, Long productId);
    void deleteEvaluation(Long id);
}
