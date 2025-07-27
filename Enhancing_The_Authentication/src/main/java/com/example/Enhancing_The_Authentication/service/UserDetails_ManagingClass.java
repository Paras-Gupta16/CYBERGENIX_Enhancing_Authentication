package com.example.Enhancing_The_Authentication.service;

import com.example.Enhancing_The_Authentication.entity.UserDetails;
import com.example.Enhancing_The_Authentication.repository.UserDatabaseMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetails_ManagingClass {

    private final UserDatabaseMapping userDatabaseMapping ;
    private final PasswordEncoder passwordEncoder;

    public UserDetails addUserDetails(UserDetails userDetails){
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userDatabaseMapping.save(userDetails);
    }

    public UserDetails findByUsername(String username) {
        return userDatabaseMapping.findByUsername(username);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
