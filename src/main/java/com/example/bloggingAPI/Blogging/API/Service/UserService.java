package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> saveEntry(User user){
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
