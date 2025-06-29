package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.PostRepository;
import com.example.bloggingAPI.Blogging.API.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //to create post for the logged in user
    public Optional<Post> saveEntry(Post post, String email){
       User user = userRepository.findByEmail(email);
        post.setUserId(user.getId());

        Post saveInput = postRepository.save(post);
        user.getAllPosts().add(saveInput);
        userService.saveEntry(user);
        postRepository.save(saveInput);

        return Optional.of(saveInput);
    }

    public void saveUserEntry(Post post){
        postRepository.save(post);
    }

    //for everyone.
    public List<Post> getAll(){
        return postRepository.findAll();
    }

    //for everyone
   public Post getPostByTitle(String title){
        return postRepository.findByTitle(title);
   }

   public Optional<Post> getById(ObjectId id){
        return postRepository.findById(id);
   }


   //update the post for the logged-in users.
   public Post updatePost(ObjectId postId, Post updatedPost, String userName){
        Optional<Post> post = Optional.ofNullable(postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found")));

        if(!userRepository.existsByUserName(userName)){
            throw new RuntimeException("unauthorized to update the post");
        }

        updatedPost.setTitle(updatedPost.getTitle());
        updatedPost.setContent(updatedPost.getContent());

        return postRepository.save(updatedPost);

   }


   //finding the post by email


   //delete the post only available for logged-in users.
   public void deleteById(ObjectId id, String userName){
        Optional<Post> post = Optional.ofNullable(postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found")));

        User user = userService.findByUserName(userName);
        user.getAllPosts().removeIf(x -> x.getId().equals(id));

        userService.saveEntry(user);
        postRepository.deleteById(id);
   }

   public void delete(){
        postRepository.deleteAll();
   }


   //delete post by title
   public void deleteByTitle(String title, String userName){
        Post toDelete = postRepository.findByTitle(title);
        User user = userRepository.findByUserName(userName);
        user.getAllPosts().removeIf(x -> x.getTitle().equals(title));
        userService.saveEntry(user);
        postRepository.delete(toDelete);
   }
}
