package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Models.UserDTO;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //admin has access to see all the users, so to fetch them altogether
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/all-users")
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
    public ResponseEntity<?> createAdminEntry(@RequestBody User request){

        User createdAdmin = userService.saveAdminUser(request.getUserName(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok("Admin has Created");
    }


    //update the user details
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/auth/update-admin")
    public ResponseEntity<?> updateAdminDetails(@RequestBody User dto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User adminUser = userRepository.findByUserName(userName);

        if(adminUser != null){
            adminUser.setUserName(dto.getUserName());
            adminUser.setPassword(encoder.encode(dto.getPassword()));
            adminUser.setEmail(dto.getEmail());
            userService.saveEntry(adminUser);
            return ResponseEntity.ok("Updated user details");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/auth/delete-user")
    public ResponseEntity<?> deleteUserByUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User toDelete = userRepository.findByUserName(userName);
        userRepository.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/dashboard")
    public ResponseEntity<?> viewAdminDashboard() {
        return ResponseEntity.ok("Welcome to the Admin Dashboard");
    }


}
