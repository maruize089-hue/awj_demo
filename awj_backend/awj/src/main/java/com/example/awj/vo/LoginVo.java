package com.example.awj.vo;

import lombok.Data;

@Data
public class LoginVo {
    private String token;
    private UserVo userInfo;
    private String role;
}
