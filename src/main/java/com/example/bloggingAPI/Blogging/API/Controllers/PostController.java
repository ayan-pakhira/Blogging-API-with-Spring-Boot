package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtService jwtService;


    //creating the posts only for login users.
    @PostMapping("/name/create-post")
    public ResponseEntity<Post> createEntry(@RequestBody Post post){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Post> saved = postService.saveEntry(post, email);

        if(saved.isPresent()){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    //only for logged in users.
    //to fetch the posts created by users
    @GetMapping("/name/{userName}")
    public ResponseEntity<?> getPostByUserName(@PathVariable String userName,
                                               @RequestHeader("Authorization") String authHeader){

        ObjectId userId = jwtService.extractUserId(authHeader);
        Optional<User> loggedInUser = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found")));

        if(!loggedInUser.get().getUserName().equals(userName)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user is invalid");
        }

        List<Post> posts = postRepository.findByUserId(userId);

        return ResponseEntity.ok(posts);

    }


    @GetMapping("/name/{userName}/{title}")
    public ResponseEntity<?> getPostByUserNameTitle(@PathVariable String userName, @PathVariable String title){
        User user = userService.findByUserName(userName);
        //Post post = postService.getPostByTitle(title);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Filtering the posts that match the given title
        List<Post> matchingPosts = user.getAllPosts()
                .stream()
                .filter(post -> post.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());

        if (matchingPosts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(matchingPosts, HttpStatus.OK);


    }





    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/name/update-post/{title}")
   public ResponseEntity<?> updatePost(@PathVariable String title, @RequestBody Post post) {

        Post postInDb = postRepository.findByTitle(title);
        if(postInDb != null){
            postInDb.setTitle(post.getTitle());
            postInDb.setContent(post.getContent());
            postService.saveUserEntry(post);
            return ResponseEntity.ok("updated the post");
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   }


   //liking a post by user
   @PutMapping("/like/{userName}/{title}")
   public Object postLikedByUser(@PathVariable String userName,
                                 @PathVariable String title,
                                 @RequestHeader("Authorization") String authHeader){
        ObjectId userId = jwtService.extractUserId(authHeader);
        User loggedInUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if(!loggedInUser.getUserName().equals(userName)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("user is invalid");
        }

        Optional<Post> post = Optional.ofNullable(postRepository.findByTitle(title));

        if(post.get().getLikedUserIds().contains(userId)){
            return ResponseEntity.badRequest().body("you already liked the post");
        }

       post.get().setLikeCount(post.get().getLikeCount() + 1);
       post.get().getLikedUserIds().add(userId);
        postService.saveUserEntry(post.orElse(null));

        return ResponseEntity.ok("post liked!!, total likes: " + post.get().getLikeCount());
   }


    //deleting the post for logged in users.
    @DeleteMapping("/name/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable ObjectId id,
            Authentication auth
    ){
        String userName = auth.getName();
        postService.deleteById(id, userName);

        return ResponseEntity.ok("post deleted");
    }



    @DeleteMapping("/")
    public boolean deleteAll(){
        postService.delete();

        return true;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/name/delete-post/{title}")
    public boolean deletePostByTitle(@PathVariable String title){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        postService.deleteByTitle(title, userName);

        return true;
    }

    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable ObjectId id, @PathVariable String userName){
        postService.deleteById(id, userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
