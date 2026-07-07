package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.EvaluationDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.EvaluationService;
import com.example.awj.vo.EvaluationVo;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final EvaluationService evaluationService;

    public ReviewController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    public Result<List<EvaluationVo>> getReviewList(@RequestParam(required = false) Long productId) {
        return Result.success(evaluationService.getEvaluationList(productId, null));
    }

    @GetMapping("/check")
    public Result<Boolean> checkReview(@RequestParam(required = false) Long orderId, @RequestParam(required = false) Long productId) {
        return Result.success(evaluationService.hasEvaluation(getCurrentUserId(), orderId, productId));
    }

    @PostMapping("/create")
    public Result<EvaluationVo> createReview(@Valid @RequestBody ReviewDto dto) {
        EvaluationDto evaluationDto = new EvaluationDto();
        evaluationDto.setOrderId(dto.getOrderId());
        evaluationDto.setProductId(dto.getProductId());
        evaluationDto.setRating(dto.getRating());
        evaluationDto.setContent(dto.getContent());
        return Result.success(evaluationService.addEvaluation(getCurrentUserId(), evaluationDto));
    }

    @lombok.Data
    public static class ReviewDto {
        private Long orderId;
        private Long productId;
        private Integer rating;
        private String content;
    }
}
