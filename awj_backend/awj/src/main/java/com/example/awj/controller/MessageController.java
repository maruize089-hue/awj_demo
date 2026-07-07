package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.security.LoginUser;
import com.example.awj.service.MessageService;
import com.example.awj.vo.MessageVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public Result<List<MessageVo>> getMessageList() {
        return Result.success(messageService.getMessageList(getCurrentUserId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Result<MessageVo> getMessageById(@PathVariable Long id) {
        return Result.success(messageService.getMessageById(getCurrentUserId(), id));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> markRead(@PathVariable Long id) {
        messageService.markRead(getCurrentUserId(), id);
        return Result.success();
    }

    @PutMapping("/read/all")
    @PreAuthorize("hasRole('USER')")
    public Result<Void> markAllRead() {
        messageService.markAllRead(getCurrentUserId());
        return Result.success();
    }
}
