package com.example.awj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.awj.entity.Merchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
    
    @Select("SELECT * FROM awj_merchant WHERE user_id = #{userId}")
    Merchant selectByUserId(Long userId);
}
