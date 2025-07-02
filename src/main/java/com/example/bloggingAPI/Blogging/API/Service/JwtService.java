package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Filter.JwtFilter;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import com.example.bloggingAPI.Blogging.API.Utils.JwtUtil;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Builder
public class JwtService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtFilter jwtFilter;


    @Autowired
    private JwtUtil jwtUtil;


    public ObjectId extractUserId(String authHeader){
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email).getId();
    }
}
