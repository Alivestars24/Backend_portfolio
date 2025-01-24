package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.payload.UserResponse;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Map<String, Object> login(LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Log input
            System.out.println("Login attempt for: " + loginDto.getUsernameOrEmail());

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsernameOrEmail(),
                            loginDto.getPassword()
                    )
            );

            // Log authentication success
            System.out.println("Authentication successful: " + authentication.isAuthenticated());

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate the JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            // Log token generation
            System.out.println("Generated Token: " + token);

            // Fetch user details
            User user = userRepository.findByUsernameOrEmail(
                    loginDto.getUsernameOrEmail(),
                    loginDto.getUsernameOrEmail()
            ).orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + loginDto.getUsernameOrEmail()));

            // Log fetched user
            System.out.println("Fetched User: " + user.getUsername());

            // Map user details to a response DTO
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRoles() // Ensure this is not null
            );

            // Log user details
            System.out.println("User Response: " + userResponse);

            // Prepare the response object
            response.put("accessToken", token);
            response.put("userDetails", userResponse);

            // Log response map
            System.out.println("Response Map: " + response);

        } catch (Exception e) {
            // Handle exceptions and log the error
            e.printStackTrace();
            response.put("accessToken", null);
            response.put("userDetails", null);
        }

        return response;
    }

    @Override
    public String register(RegisterDto registerDto) {
        System.out.println("Hello world");

        // Check if username exists in the database
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        // Check if email exists in the database
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Set a default role as "ROLE_USER"
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            // Create and save the role if it doesn't exist
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            return roleRepository.save(newRole);
        });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public void deleteUser(String token) {
        // Remove "Bearer " prefix from the token
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract username from the token
        String username = jwtTokenProvider.getUsername(token);

        // Fetch the user from the database
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", username));

        // Remove user roles associations
        user.getRoles().clear();
        userRepository.save(user);

        // Delete the user
        userRepository.delete(user);
    }
}
