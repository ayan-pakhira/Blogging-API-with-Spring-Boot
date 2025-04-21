package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //registering as an user
    public User saveEntry(User user){

        User user1 = userRepository.findByUserName(user.getUserName());

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);

    }

    //for public controller
    public User saveUserEntry(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //for registering as an admin
    public User saveAdminUser(String userName, String password, String email){
        User admin = new User();

        admin.setPassword(encoder.encode(password));
        admin.setUserName(userName);
        admin.setEmail(email);
        admin.setRoles(Arrays.asList("ADMIN", "USER"));
        return userRepository.save(admin);
    }


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
