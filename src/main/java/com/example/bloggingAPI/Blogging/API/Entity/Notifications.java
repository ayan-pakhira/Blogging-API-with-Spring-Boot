package com.example.bloggingAPI.Blogging.API.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    @Id
    private ObjectId id;

    private ObjectId recipientId;
    private ObjectId senderId;
    private String type;
    private String message;
    private ObjectId postId;
    private boolean isRead = false;
    private Date createdAt = new Date();
}
