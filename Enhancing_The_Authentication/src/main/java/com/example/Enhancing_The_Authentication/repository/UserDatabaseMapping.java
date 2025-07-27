package com.example.Enhancing_The_Authentication.repository;

import com.example.Enhancing_The_Authentication.entity.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDatabaseMapping extends MongoRepository<UserDetails,String> {
    UserDetails findByUsername(String username);
}
