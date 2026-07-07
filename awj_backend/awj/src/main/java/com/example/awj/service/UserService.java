package com.example.awj.service;

import com.example.awj.dto.LoginDto;
import com.example.awj.dto.RegisterDto;
import com.example.awj.dto.WxLoginDto;
import com.example.awj.vo.LoginVo;
import com.example.awj.vo.UserVo;

public interface UserService {
    LoginVo login(LoginDto dto);
    UserVo register(RegisterDto dto);
    LoginVo wxLogin(WxLoginDto dto);
    UserVo getUserInfo(Long userId);
}
