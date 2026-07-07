package com.example.awj.service.impl;

import com.example.awj.common.result.Result;
import com.example.awj.common.utils.JwtUtils;
import com.example.awj.dto.LoginDto;
import com.example.awj.dto.RegisterDto;
import com.example.awj.dto.WxLoginDto;
import com.example.awj.entity.User;
import com.example.awj.mapper.UserMapper;
import com.example.awj.security.LoginUser;
import com.example.awj.service.UserService;
import com.example.awj.vo.LoginVo;
import com.example.awj.vo.UserVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, 
                          AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginVo login(LoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = jwtUtils.generateToken(loginUser.getId(), loginUser.getUsername(), loginUser.getRole());
        
        LoginVo vo = new LoginVo();
        vo.setToken(token);
        vo.setRole(loginUser.getRole());
        vo.setUserInfo(convertToVo(userMapper.selectById(loginUser.getId())));
        
        return vo;
    }

    @Override
    @Transactional
    public UserVo register(RegisterDto dto) {
        if (userMapper.selectByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (dto.getPhone() != null && userMapper.selectByPhone(dto.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole("USER");
        user.setStatus(1);
        
        userMapper.insert(user);
        return convertToVo(user);
    }

    @Override
    public LoginVo wxLogin(WxLoginDto dto) {
        String openId = "mock_openid_" + System.currentTimeMillis();
        
        User user = userMapper.selectByUsername(openId);
        if (user == null) {
            user = new User();
            user.setUsername(openId);
            user.setPassword(passwordEncoder.encode("wx_" + openId));
            user.setRole("USER");
            user.setStatus(1);
            userMapper.insert(user);
        }
        
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        LoginVo vo = new LoginVo();
        vo.setToken(token);
        vo.setRole(user.getRole());
        vo.setUserInfo(convertToVo(user));
        
        return vo;
    }

    @Override
    public UserVo getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToVo(user);
    }

    private UserVo convertToVo(User user) {
        UserVo vo = new UserVo();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        return vo;
    }
}
