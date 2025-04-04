package com.example.bloggingAPI.Blogging.API.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "comments")
@Data
@NoArgsConstructor
public class Comments {

    @Id
    private ObjectId id;

    @NonNull
    @Indexed(unique = true)
    private String content;
}
