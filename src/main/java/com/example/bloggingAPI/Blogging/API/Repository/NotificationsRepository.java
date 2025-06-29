package com.example.bloggingAPI.Blogging.API.Repository;

import com.example.bloggingAPI.Blogging.API.Entity.Notifications;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NotificationsRepository extends MongoRepository<Notifications, ObjectId> {
    List<Notifications> findByRecipientIdOrderByCreatedAtDesc(ObjectId recipientId);
}
