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

@Document(collection = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String title;

    @NonNull
    private String content;

    private ObjectId userId;

    @DBRef
    private List<Comments> allComments = new ArrayList<>();

}
