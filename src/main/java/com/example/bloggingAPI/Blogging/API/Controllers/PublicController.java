package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;


    @PostMapping("/create-user/")
    public boolean createNewEntry(@RequestBody User user){
        userService.saveUserEntry(user);

        return true;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll(){

        List<User> all = userService.getAll();

        if(all != null){
            return new ResponseEntity<List<User>>(all, HttpStatus.OK);
        }

        return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfTokenValue(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

}
