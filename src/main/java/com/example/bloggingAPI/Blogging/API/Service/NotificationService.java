package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.Notifications;
import org.bson.types.ObjectId;

import java.util.*;
public interface NotificationService {

    Notifications createNotifications (Notifications notification);
    List<Notifications> getNotificationByRecipient(ObjectId recipientId);
    void markNotificationAsRead(ObjectId notificationId);
    void markAllAsRead(ObjectId recipientId);

}
