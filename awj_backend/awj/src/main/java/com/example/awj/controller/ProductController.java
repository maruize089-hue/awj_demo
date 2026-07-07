package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.PageDto;
import com.example.awj.dto.ProductDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.ProductService;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ProductVo;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public Result<List<ProductVo>> getProductList(@RequestParam(required = false) Long categoryId) {
        return Result.success(productService.getProductList(categoryId));
    }

    @GetMapping("/{id}")
    public Result<ProductVo> getProductById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @GetMapping("/search")
    public Result<PageResult<ProductVo>> searchProduct(PageDto dto) {
        return Result.success(productService.searchProduct(dto));
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<ProductVo> addProduct(@Valid @RequestBody ProductDto dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(productService.addProduct(loginUser.getId(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<ProductVo> updateProduct(@Valid @RequestBody ProductDto dto) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(productService.updateProduct(loginUser.getId(), dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MERCHANT')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        productService.deleteProduct(loginUser.getId(), id);
        return Result.success();
    }
}
