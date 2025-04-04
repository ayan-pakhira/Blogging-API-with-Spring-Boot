package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
    Post findByTitle(String title);
}
