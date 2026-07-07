package com.example.awj.service.impl;

import com.example.awj.dto.CategoryDto;
import com.example.awj.entity.Category;
import com.example.awj.mapper.CategoryMapper;
import com.example.awj.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getCategoryList(String type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Category::getType, type);
        }
        wrapper.orderByAsc(Category::getSortOrder);
        
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        return category;
    }

    @Override
    @Transactional
    public Category addCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        category.setType(dto.getType() != null ? dto.getType() : "PRODUCT");
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        category.setStatus(1);
        
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryDto dto) {
        Category category = categoryMapper.selectById(dto.getId());
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        
        category.setName(dto.getName());
        category.setParentId(dto.getParentId() != null ? dto.getParentId() : category.getParentId());
        category.setType(dto.getType() != null ? dto.getType() : category.getType());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : category.getSortOrder());
        
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        categoryMapper.deleteById(id);
    }
}
