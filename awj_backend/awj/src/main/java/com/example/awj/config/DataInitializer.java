package com.example.awj.config;

import com.example.awj.entity.User;
import com.example.awj.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createOrUpdateUser("admin", "13800138000", "管理员", "ADMIN");
        createOrUpdateUser("test", "13800138001", "测试用户", "USER");
        createOrUpdateUser("merchant1", "13800138002", "商家张三", "MERCHANT");
    }

    private void createOrUpdateUser(String username, String phone, String realName, String role) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPhone(phone);
            user.setRealName(realName);
            user.setRole(role);
            user.setStatus(1);
            userMapper.insert(user);
        }
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateById(user);
    }
}