package com.example.awj.service;

import com.example.awj.dto.CategoryDto;
import com.example.awj.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList(String type);
    Category getCategoryById(Long id);
    Category addCategory(CategoryDto dto);
    Category updateCategory(CategoryDto dto);
    void deleteCategory(Long id);
}
