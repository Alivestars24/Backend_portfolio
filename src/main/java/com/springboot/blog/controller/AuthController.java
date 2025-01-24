package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.payload.UserResponse;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        // Call the AuthService login method to get the response map
        Map<String, Object> response = authService.login(loginDto);

        // Extract the token from the response
        String token = (String) response.get("accessToken");

        // Extract user details from the response
        UserResponse userResponse = (UserResponse) response.get("userDetails");

        // Create JWTAuthResponse and populate it
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setUserDetails(userResponse);

        // Return the response entity
        return ResponseEntity.ok(jwtAuthResponse);
    }
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        System.out.println("Generated Token: ");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        authService.deleteUser(token);
        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }
}