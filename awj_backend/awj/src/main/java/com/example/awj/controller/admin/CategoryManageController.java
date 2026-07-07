package com.example.awj.controller.admin;

import com.example.awj.common.result.Result;
import com.example.awj.dto.CategoryDto;
import com.example.awj.entity.Category;
import com.example.awj.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryManageController {

    private final CategoryService categoryService;

    public CategoryManageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Result<Category> addCategory(@Valid @RequestBody CategoryDto dto) {
        return Result.success(categoryService.addCategory(dto));
    }

    @PutMapping("/{id}")
    public Result<Category> updateCategory(@Valid @RequestBody CategoryDto dto) {
        return Result.success(categoryService.updateCategory(dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
