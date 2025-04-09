package com.example.bloggingAPI.Blogging.API.Controllers;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity<?> createEntry(@RequestBody User user){
        User saved = userService.saveEntry(user);

        if(saved != null){
            return new ResponseEntity<User>(HttpStatus.CREATED);
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll(){

        List<User> all = userService.getAll();

        if(all != null){
            return new ResponseEntity<List<User>>(all, HttpStatus.OK);
        }

        return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
    }


//    @PutMapping
//    public ResponseEntity<?> updateUser(@RequestBody User user){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//
//        User userInDb = userService.findByUserName(userName);
//        if(userInDb != null){
//            userInDb.setUserName(user.getUserName());
//            userInDb.setPassword(user.getPassword());
//            userService.saveUser(user);
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }




    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){
        User users = userService.findByUserName(userName);

        if(users != null){
            return new ResponseEntity<User> (users, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/")
    public boolean deleteAllUsers(){
        userService.deleteAll();
        return true;
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<?> deletedByUserName(@PathVariable String userName){
        Optional<User> deleted = userService.deleteByUserName(userName);

        if(deleted.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
