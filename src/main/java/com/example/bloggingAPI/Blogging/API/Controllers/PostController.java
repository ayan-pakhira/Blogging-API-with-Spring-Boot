package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Service.PostService;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    //creating the posts only for logged in users.
    @PostMapping("/name/create-post")
    public ResponseEntity<Post> createEntry(@RequestBody Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<Post> saved = postService.saveEntry(post, userName);

        if(saved.isEmpty()){
            return new ResponseEntity<Post>(HttpStatus.CREATED);
        }

        return new ResponseEntity<Post>(HttpStatus.BAD_REQUEST);
    }

    //this is public, available for everyone
    @GetMapping("/")
    public List<Post> getAllPost(){
        return postService.getAll();
    }

    @GetMapping("/id/{title}")
    public Post getPostByTitle(@PathVariable String title){
        return postService.getPostByTitle(title);
    }


    //only for logged in users.
    @GetMapping("/name/{userName}")
    public ResponseEntity<?> getPostByUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //fetching the username from the authentication itself, means for authorization purpose when we will
        //enter username and password, from there we will fetch the username through this process
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        if(user == null){
            return new ResponseEntity<List<Post>>(HttpStatus.NOT_FOUND);
        }

        List<Post> all = user.getAllPosts();

        if(userName.equals(user)){

                return new ResponseEntity<>(all, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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


//    @PutMapping("/id/{userName}/{id}")
//    public ResponseEntity<?> updatePostById(
//            @PathVariable ObjectId id,
//            @PathVariable Post newPost,
//            @PathVariable String userName
//    ){
//        Post oldPost = postService.getById(id).orElse(null);
//        if(oldPost != null){
//            oldPost.setTitle(newPost.getTitle() != null && !newPost.getTitle().equals("") ? newPost.getTitle() : oldPost.getTitle());
//            oldPost.setContent((newPost.getContent() != null && !newPost.getContent().equals("") ? newPost.getContent() : oldPost.getContent()));
//            postService.saveEntry(oldPost);
//            return new ResponseEntity<>(oldPost, HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }


    //updating the post for logged in users.
    @PutMapping("/name/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable ObjectId id,
            @RequestBody Post post,
            Authentication auth
    ){
        String userName = auth.getName();
        postService.updatePost(id, post, userName);

        return ResponseEntity.ok("Updated");
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

    @DeleteMapping("/{title}")
    public boolean deletePostByTitle(@PathVariable String title){
        postService.deleteByTitle(title);

        return true;
    }

    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable ObjectId id, @PathVariable String userName){
        postService.deleteById(id, userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
