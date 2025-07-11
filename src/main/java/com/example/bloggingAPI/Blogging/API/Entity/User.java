package com.example.bloggingAPI.Blogging.API.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @NonNull
    private String userName;

    @NonNull
    private String password;

    @NonNull
    @Indexed(unique = true)
    private String email;

    @DBRef
    private List<Post> allPosts = new ArrayList<>();

    private List<String> roles;
}
