package com.example.awj.service;

import com.example.awj.dto.PageDto;
import com.example.awj.dto.ProductDto;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ProductVo;

import java.util.List;

public interface ProductService {
    List<ProductVo> getProductList(Long categoryId);
    ProductVo getProductById(Long id);
    PageResult<ProductVo> searchProduct(PageDto dto);
    ProductVo addProduct(Long merchantId, ProductDto dto);
    ProductVo updateProduct(Long merchantId, ProductDto dto);
    void deleteProduct(Long merchantId, Long id);
}
