package com.example.bloggingAPI.Blogging.API.Filter;


import com.example.bloggingAPI.Blogging.API.Service.CustomUserDetailsService;
import com.example.bloggingAPI.Blogging.API.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import java.util.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
   private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String userName = null;
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(jwt);
        }
        if (userName != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

}

