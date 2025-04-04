package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);

    User deleteByUserName(String userName);
}
