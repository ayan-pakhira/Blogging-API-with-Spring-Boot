package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public Optional<Post> saveEntry(Post post, String userName){
        User user = userService.findByUserName(userName);
        Post saveInput = postRepository.save(post);
        user.getAllPosts().add(saveInput);
        userService.saveEntry(user);

        return Optional.empty();
    }

    public void saveUserEntry(Post post){
        postRepository.save(post);
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

   public Post getPostByTitle(String title){
        return postRepository.findByTitle(title);
   }

   public Optional<Post> getById(ObjectId id){
        return postRepository.findById(id);
   }


   public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getAllPosts().removeIf(x -> x.getId().equals(id));

        userService.saveEntry(user);
        postRepository.deleteById(id);
   }

   public void delete(){
        postRepository.deleteAll();
   }

   public void deleteByTitle(String title){
        Post toDelete = postRepository.findByTitle(title);
        postRepository.delete(toDelete);
   }
}
