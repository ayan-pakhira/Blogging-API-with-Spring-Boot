package com.example.bloggingAPI.Blogging.API.Controllers;

import com.example.bloggingAPI.Blogging.API.Entity.Notifications;
import com.example.bloggingAPI.Blogging.API.Repository.NotificationsRepository;
import com.example.bloggingAPI.Blogging.API.Service.NotificationServiceImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Autowired
    private NotificationServiceImpl notificationService;

    //creating notifications
    @PostMapping("/notify")
    public ResponseEntity<?> generateNotification(@RequestBody Notifications notifications){
        Notifications saved = notificationService.createNotifications(notifications);
        return ResponseEntity.ok(saved);
    }


    //get all notifications for an user
    @GetMapping("/alert/{recipientId}")
    public ResponseEntity<List<Notifications>> getNotifications(@PathVariable ObjectId recipientId){
        List<Notifications> notifications = notificationService.getNotificationByRecipient(recipientId);
        return ResponseEntity.ok(notifications);
    }


    //mark a single notification as read
    //id should be the notification id
    @PutMapping("/marked-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable ObjectId id){
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok("notification marked as read");
    }

    //mark all notifications as read
    @PutMapping("/all-read/{recipientId}")
    public ResponseEntity<?> markAllAsRead(@PathVariable ObjectId recipientId){
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.ok("all the notifications are marked as read");
    }
}
