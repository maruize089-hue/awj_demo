package com.example.awj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.awj.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
