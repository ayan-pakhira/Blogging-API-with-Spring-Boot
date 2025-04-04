package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.Comments;
import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Repository.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    public Optional<Comments> saveEntry(Comments comments, String userName, String title){
        User user = userService.findByUserName(userName);
        Post post = postService.getPostByTitle(title);

        if(user == null || post == null){
            return Optional.empty();
        }
        Comments saveComment = commentRepository.save(comments);
        post.getAllComments().add(saveComment);
        postService.saveUserEntry(post);


        return Optional.of(saveComment);

    }

    public void delete(){
        commentRepository.deleteAll();
    }

    public void deleteComments(String title, ObjectId id){
        Post post = postService.getPostByTitle(title);
       // User user = userService.findByUserName(userName);

        post.getAllComments().removeIf(x -> x.getId().equals(id));

        postService.saveUserEntry(post);
        commentRepository.deleteById(id);

    }



}
