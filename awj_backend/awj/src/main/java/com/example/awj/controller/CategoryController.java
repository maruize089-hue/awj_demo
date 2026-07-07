package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.CategoryDto;
import com.example.awj.entity.Category;
import com.example.awj.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(@RequestParam(required = false) String type) {
        return Result.success(categoryService.getCategoryList(type));
    }
}
