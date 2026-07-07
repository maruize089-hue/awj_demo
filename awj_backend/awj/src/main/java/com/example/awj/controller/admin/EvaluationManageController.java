package com.example.awj.controller.admin;

import com.example.awj.common.result.Result;
import com.example.awj.dto.PageDto;
import com.example.awj.service.EvaluationService;
import com.example.awj.vo.EvaluationVo;
import com.example.awj.common.result.PageResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/evaluations")
public class EvaluationManageController {

    private final EvaluationService evaluationService;

    public EvaluationManageController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/list")
    public Result<PageResult<EvaluationVo>> getEvaluationList(PageDto dto) {
        return Result.success(evaluationService.getEvaluationList(dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return Result.success();
    }
}
