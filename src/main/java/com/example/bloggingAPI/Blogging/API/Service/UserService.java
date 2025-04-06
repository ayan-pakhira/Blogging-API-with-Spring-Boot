package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> saveEntry(User user){
        userRepository.save(user);
        return Optional.empty();
    }

    public void saveUserEntry(User user){
        userRepository.save(user);
    }



    //to save the user and encrypted password in the database.
    public Optional<User> saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList());
        userRepository.save(user);

        return Optional.empty();
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
