package com.example.awj.service.impl;

import com.example.awj.dto.PageDto;
import com.example.awj.dto.ProductDto;
import com.example.awj.entity.Category;
import com.example.awj.entity.Merchant;
import com.example.awj.entity.Product;
import com.example.awj.mapper.CategoryMapper;
import com.example.awj.mapper.MerchantMapper;
import com.example.awj.mapper.ProductMapper;
import com.example.awj.service.ProductService;
import com.example.awj.common.result.PageResult;
import com.example.awj.vo.ProductVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final MerchantMapper merchantMapper;

    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper, 
                            MerchantMapper merchantMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.merchantMapper = merchantMapper;
    }

    @Override
    public List<ProductVo> getProductList(Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Product::getCreateTime);
        
        return productMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public ProductVo getProductById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        return convertToVo(product);
    }

    @Override
    public PageResult<ProductVo> searchProduct(PageDto dto) {
        Page<Product> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(Product::getName, dto.getKeyword());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        
        Page<Product> result = productMapper.selectPage(page, wrapper);
        
        List<ProductVo> records = result.getRecords().stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
        
        return new PageResult<ProductVo>(result.getTotal(), result.getCurrent(), result.getSize(), records);
    }

    @Override
    @Transactional
    public ProductVo addProduct(Long merchantId, ProductDto dto) {
        Product product = new Product();
        product.setMerchantId(merchantId);
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setTotalStock(dto.getTotalStock() != null ? dto.getTotalStock() : 0);
        product.setStatus(0);
        
        productMapper.insert(product);
        return convertToVo(product);
    }

    @Override
    @Transactional
    public ProductVo updateProduct(Long merchantId, ProductDto dto) {
        Product product = productMapper.selectById(dto.getId());
        if (product == null || !product.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("商品不存在或无权限");
        }
        
        product.setCategoryId(dto.getCategoryId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setTotalStock(dto.getTotalStock() != null ? dto.getTotalStock() : product.getTotalStock());
        
        productMapper.updateById(product);
        return convertToVo(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long merchantId, Long id) {
        Product product = productMapper.selectById(id);
        if (product == null || !product.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("商品不存在或无权限");
        }
        productMapper.deleteById(id);
    }

    private ProductVo convertToVo(Product product) {
        ProductVo vo = new ProductVo();
        vo.setId(product.getId());
        vo.setMerchantId(product.getMerchantId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setDescription(product.getDescription());
        vo.setImage(product.getImage());
        vo.setTotalStock(product.getTotalStock());
        vo.setPrice(product.getPrice());
        vo.setSales(product.getSales());
        vo.setStatus(product.getStatus().equals(1) ? "上架" : "下架");
        
        Merchant merchant = merchantMapper.selectById(product.getMerchantId());
        if (merchant != null) {
            vo.setMerchantName(merchant.getName());
        }
        
        Category category = categoryMapper.selectById(product.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        
        return vo;
    }
}
