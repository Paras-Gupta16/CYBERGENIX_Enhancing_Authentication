package com.example.Enhancing_The_Authentication.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@Document(collection = "UserDatabase_ServiceRequest")
public class UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private List<String> roles;
}
