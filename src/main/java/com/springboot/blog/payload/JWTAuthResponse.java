package com.springboot.blog.payload;

import com.springboot.blog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    private String accessToken;
    private UserResponse userDetails;

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserResponse getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserResponse userDetails) {
        this.userDetails = userDetails;
    }
}

