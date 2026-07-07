package com.example.awj.service;

import com.example.awj.dto.PageDto;
import com.example.awj.vo.MessageVo;
import com.example.awj.common.result.PageResult;

import java.util.List;

public interface MessageService {
    List<MessageVo> getMessageList(Long userId);
    MessageVo getMessageById(Long userId, Long id);
    void markRead(Long userId, Long id);
    void markAllRead(Long userId);
}
