package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Models.UserDTO;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    //admin has access to see all the users, so to fetch them altogether
    @GetMapping("/api/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> all = userService.getAll();

        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //creating the admin
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/auth/create-admin")
    public ResponseEntity<?> createAdminEntry(@RequestBody UserDTO request){

        User createdAdmin = userService.saveAdminUser(request.getUserName(), request.getPassword());
        return ResponseEntity.ok("Admin has Created");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/dashboard")
    public ResponseEntity<?> viewAdminDashboard() {
        return ResponseEntity.ok("Welcome to the Admin Dashboard");
    }


}
