package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.Comments;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comments, ObjectId> {

}
