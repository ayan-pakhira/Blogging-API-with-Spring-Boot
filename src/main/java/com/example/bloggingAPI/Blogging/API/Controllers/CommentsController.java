package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.Comments;
import com.example.bloggingAPI.Blogging.API.Entity.Post;
import com.example.bloggingAPI.Blogging.API.Entity.User;
import com.example.bloggingAPI.Blogging.API.Service.CommentService;
import com.example.bloggingAPI.Blogging.API.Service.PostService;
import com.example.bloggingAPI.Blogging.API.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    @PostMapping("/create-comments/{userName}/{title}")
    public ResponseEntity<?> createComments(@RequestBody Comments comments, @PathVariable String userName, @PathVariable String title){

        Optional<Comments> saved = commentService.saveEntry(comments, userName, title);

        if(saved.isPresent()){
            return new ResponseEntity<Comments>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/")
    public boolean deleteAll(){
        commentService.delete();

        return true;
    }

    @DeleteMapping("/id/{title}/{id}")
    public ResponseEntity<?> deleteCommentsById(@PathVariable ObjectId id, @PathVariable String title){

        commentService.deleteComments(title, id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
