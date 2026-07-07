package com.example.awj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.awj.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM awj_user WHERE username = #{username}")
    User selectByUsername(String username);
    
    @Select("SELECT * FROM awj_user WHERE phone = #{phone}")
    User selectByPhone(String phone);
}
