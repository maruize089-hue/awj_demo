package com.example.awj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.awj.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT status, COUNT(*) as count FROM awj_product_order WHERE user_id = #{userId} GROUP BY status")
    List<OrderStatusCount> getOrderStats(Long userId);
    
    @lombok.Data
    class OrderStatusCount {
        private String status;
        private Integer count;
    }
}
