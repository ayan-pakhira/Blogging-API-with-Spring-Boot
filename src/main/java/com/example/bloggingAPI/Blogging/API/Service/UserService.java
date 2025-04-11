package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

//    @Autowired
//    private JWTService jwtService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User saveEntry(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

    public User saveUserEntry(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // to verify the user while logging in.
//    public String verify(User user){
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//
//        if(authentication.isAuthenticated()){
//            return jwtService.generateToken(user.getUserName());
//        }
//        return "Fail";
//    }


    //to save the user and encrypted password in the database.
//    public Optional<User> saveUser(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(Arrays.asList());
//        userRepository.save(user);
//
//        return Optional.empty();
//    }

    public List<User> getAll(){
        return userRepository.findAll();
    }


    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public Optional<User> deleteByUserName(String userName){
        User deleted = userRepository.deleteByUserName(userName);

        return Optional.empty();
    }

}
