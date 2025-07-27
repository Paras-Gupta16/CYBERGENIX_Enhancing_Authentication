package com.example.Enhancing_The_Authentication.controller;

import com.example.Enhancing_The_Authentication.entity.UserDetails;
import com.example.Enhancing_The_Authentication.service.UserDetails_ManagingClass;
import com.example.Enhancing_The_Authentication.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class User_Browser_InteractClass {

    private final UserDetails_ManagingClass userDetailsManagingClass;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDetails userDetails){
        // Validate required fields
        if (!StringUtils.hasText(userDetails.getUsername()) || !StringUtils.hasText(userDetails.getPassword())) {
            return new ResponseEntity<>("Username and password are required", HttpStatus.BAD_REQUEST);
        }
        // Check for duplicate username
        if (userDetailsManagingClass.findByUsername(userDetails.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }
        if (userDetails.getRoles() == null) {
            userDetails.setRoles(Collections.singletonList("USER"));
        }
        userDetailsManagingClass.addUserDetails(userDetails);
        return new ResponseEntity<>("User Registered", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDetails loginRequest) {
        if (!StringUtils.hasText(loginRequest.getUsername()) || !StringUtils.hasText(loginRequest.getPassword())) {
            return new ResponseEntity<>("Username and password are required", HttpStatus.BAD_REQUEST);
        }
        UserDetails user = userDetailsManagingClass.findByUsername(loginRequest.getUsername());
        if (user != null && userDetailsManagingClass.getPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        return ResponseEntity.ok("You have access to a protected endpoint!");
    }
}
