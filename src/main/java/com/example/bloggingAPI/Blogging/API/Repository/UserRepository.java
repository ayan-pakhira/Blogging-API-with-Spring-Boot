package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
    User findByEmail(String email);

    List<User> findByUserNameContainingIgnoreCase(String userName);

    User deleteByUserName(String userName);

   boolean existsByUserName(String userName);
}
