package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, ObjectId> {
    Post findByTitle(String title);
    Post deleteByUserId(ObjectId id);
    List<Post> findByUserId(ObjectId id);
}
