package com.example.awj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.awj.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
