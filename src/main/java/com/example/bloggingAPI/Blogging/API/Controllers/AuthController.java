package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Models.UserDTO;
import com.example.bloggingAPI.Blogging.API.Service.CustomUserDetailsService;
import com.example.bloggingAPI.Blogging.API.Utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;






    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO user){

        try{
            auth.authenticate(new UsernamePasswordAuthenticationToken
                    (user.getUserName(), user.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping("/api/logout")
    public ResponseEntity<?> logoutPage(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(false);

        if(session != null){
            session.invalidate();
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of(
                "message", "logged Out Successfully"
        ));
    }
}
