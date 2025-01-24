package com.springboot.blog.service;

import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    void deleteUser(String token);
}
