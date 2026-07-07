package com.example.awj.controller;

import com.example.awj.common.result.Result;
import com.example.awj.dto.LoginDto;
import com.example.awj.dto.RegisterDto;
import com.example.awj.dto.WxLoginDto;
import com.example.awj.security.LoginUser;
import com.example.awj.service.UserService;
import com.example.awj.vo.LoginVo;
import com.example.awj.vo.UserVo;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginVo> login(@Valid @RequestBody LoginDto dto) {
        return Result.success(userService.login(dto));
    }

    @PostMapping("/register")
    public Result<UserVo> register(@Valid @RequestBody RegisterDto dto) {
        return Result.success(userService.register(dto));
    }

    @PostMapping("/wx-login")
    public Result<LoginVo> wxLogin(@Valid @RequestBody WxLoginDto dto) {
        return Result.success(userService.wxLogin(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.success();
    }

    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("ok");
    }
}
