package com.example.awj.service.impl;

import com.example.awj.entity.Message;
import com.example.awj.mapper.MessageMapper;
import com.example.awj.service.MessageService;
import com.example.awj.vo.MessageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public List<MessageVo> getMessageList(Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getUserId, userId)
               .orderByDesc(Message::getIsRead)
               .orderByDesc(Message::getCreateTime);
        
        return messageMapper.selectList(wrapper).stream()
            .map(this::convertToVo)
            .collect(Collectors.toList());
    }

    @Override
    public MessageVo getMessageById(Long userId, Long id) {
        Message message = messageMapper.selectById(id);
        if (message == null || !message.getUserId().equals(userId)) {
            throw new RuntimeException("消息不存在");
        }
        return convertToVo(message);
    }

    @Override
    @Transactional
    public void markRead(Long userId, Long id) {
        Message message = messageMapper.selectById(id);
        if (message == null || !message.getUserId().equals(userId)) {
            throw new RuntimeException("消息不存在");
        }
        message.setIsRead(1);
        messageMapper.updateById(message);
    }

    @Override
    @Transactional
    public void markAllRead(Long userId) {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getUserId, userId)
               .eq(Message::getIsRead, 0);
        
        List<Message> messages = messageMapper.selectList(wrapper);
        messages.forEach(m -> {
            m.setIsRead(1);
            messageMapper.updateById(m);
        });
    }

    private MessageVo convertToVo(Message message) {
        MessageVo vo = new MessageVo();
        vo.setId(message.getId());
        vo.setTitle(message.getTitle());
        vo.setContent(message.getContent());
        vo.setType(message.getType());
        vo.setTypeText(getTypeText(message.getType()));
        vo.setIsRead(message.getIsRead());
        vo.setCreateTime(message.getCreateTime());
        return vo;
    }

    private String getTypeText(String type) {
        return switch (type) {
            case "SYSTEM" -> "系统消息";
            case "ORDER" -> "订单消息";
            case "REFUND" -> "退款消息";
            default -> type;
        };
    }
}
