package com.example.bloggingAPI.Blogging.API.Controllers;
import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Models.UserPrincipal;
import com.example.bloggingAPI.Blogging.API.Repository.PostRepository;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import com.example.bloggingAPI.Blogging.API.Service.JwtService;
import com.example.bloggingAPI.Blogging.API.Service.PostService;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtService jwtService;


    //for registering the user
    @PostMapping("/auth/register")
    public ResponseEntity<?> createEntry(@RequestBody User user){
        User saved = userService.saveEntry(user);

        if(saved != null){
            return new ResponseEntity<User>(HttpStatus.CREATED);
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }


    //edit the user details
    @PreAuthorize(("hasRole('ADMIN'), ('USER')"))
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDb = userService.findByUserName(userName);
        if(userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userInDb.setRoles(Arrays.asList("USER"));
            userService.saveEntry(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //endpoints of search api where we can search for an user by their username.
    @GetMapping("/search/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){

       List<User> users =  userService.getByUserName(userName);



        if(users != null){
            return new ResponseEntity<> (users, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    //delete all users at a time
    @DeleteMapping("/auth/delete-all")
    public boolean deleteAllUsers(){
        userService.deleteAll();
        return true;
    }


    //delete the posts along with the users
    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<?> deletePostsWithUser(@PathVariable String userName,
                                                 @RequestHeader("Authorization") String authHeader){

        ObjectId userId = jwtService.extractUserId(authHeader);
        User loggedInUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if(!loggedInUser.getUserName().equals(userName)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user is invalid");
        }

        List<Post> posts = postRepository.findByUserId(userId);

        userRepository.delete(loggedInUser);
        postRepository.deleteAll(posts);

        return ResponseEntity.ok("deleted!!");
    }




}
