package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.PostRepository;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import com.example.bloggingAPI.Blogging.API.Service.PostService;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PostRepository postRepository;

    @PostMapping("/create-user/")
    public boolean createNewEntry(@RequestBody User user){
        userService.saveUserEntry(user);

        return true;
    }

    //to only read the post without login.
    @GetMapping("/all-post")
    public ResponseEntity<List<Post>> getAllValue(){
        List<Post> all = postService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/post-by-title")
    public ResponseEntity<?> getPostByTitle(@PathVariable String title){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        Post searchPost = postRepository.findByTitle(title);
        if(searchPost != null){
            return new ResponseEntity<>(searchPost, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @GetMapping("/csrf-token")
    public CsrfToken csrfTokenValue(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

}
