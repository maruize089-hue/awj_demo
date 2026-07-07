package com.example.awj.vo;

import lombok.Data;

@Data
public class UserVo {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private String realName;
    private String role;
}
