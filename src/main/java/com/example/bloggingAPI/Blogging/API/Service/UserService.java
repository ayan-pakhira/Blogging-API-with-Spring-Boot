package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.PostRepository;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtService jwtService;



    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //registering as an user
    public User saveEntry(User user){

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);

    }

    //for public controller
    public User saveUserEntry(User user){
        return userRepository.save(user);
    }

    //for registering as an admin
    public User saveAdminUser(String userName, String password, String email){
        User admin = new User();

        admin.setPassword(encoder.encode(password));
        admin.setUserName(userName);
        admin.setEmail(email);
        admin.setRoles(Arrays.asList("USER", "ADMIN"));
        return userRepository.save(admin);
    }


    public List<User> getAll(){
        return userRepository.findAll();
    }


    public User findByUserName(String userName){
        User users =  userRepository.findByUserName(userName);
        return null;
    }

    //for search api
    public List<User> getByUserName(String userName){
        return userRepository.findByUserNameContainingIgnoreCase(userName);
    }





    public void deleteAll(){
        userRepository.deleteAll();
    }

//    public Optional<User> deleteByUserName(String userName){
//        User deleted = userRepository.findByUserName(userName);
//
//        if(deleted == null){
//            throw new RuntimeException("user not found");
//        }
//
//
//
//        return Optional.empty();
//    }

}
