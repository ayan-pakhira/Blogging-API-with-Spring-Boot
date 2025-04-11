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
        Post post = postService.getPostByTitle(title); //need to replace this with the slug of the title.

        if(user == null || post == null){
            return Optional.empty();
        }
        Comments saveComment = commentRepository.save(comments);
        post.getAllComments().add(saveComment);
        postService.saveUserEntry(post);


        return Optional.of(saveComment);

    }

    public List<Comments> getAllComments(){
        return commentRepository.findAll();
    }

    public Optional<Post> getCommentsByUserName(String userName, String title) {
        User user = userService.findByUserName(userName);
        Post comments = null;
        if (user.getUserName().equals(userName)) {
            comments = postService.getPostByTitle(title);
        }
        assert comments != null;
        //a basic assertion in Java used to ensure that the comments object is not null at runtime.
        //If comments is null, it will throw an AssertionError and stop the program (if assertions are enabled).
        return Optional.of(comments);
    }

    //alternative1 of the getCommentsByUserName code.
    public Optional<Post> getPostByUserAndTitle(String userName, String title) {
        User user = userService.findByUserName(userName);

        if (user == null) {
            return Optional.empty();
        }

        Post post = postService.getPostByTitle(title);

        if (post == null) {
            return Optional.empty();
        }

        return Optional.of(post);
    }


    //alternative 2 of the getCommentByUserName code.
    public Optional<List<Comments>> getCommentsByUserAndTitle(String userName, String title) {
        User user = userService.findByUserName(userName);

        if (user == null) {
            return Optional.empty();
        }

        Post post = postService.getPostByTitle(title);

        if (post == null) {
            return Optional.empty();
        }

        return Optional.of(post.getAllComments());
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
