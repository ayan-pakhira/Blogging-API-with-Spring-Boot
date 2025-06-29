package com.example.bloggingAPI.Blogging.API.Service;

import com.example.bloggingAPI.Blogging.API.Entity.Notifications;
import com.example.bloggingAPI.Blogging.API.Repository.NotificationsRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationsRepository notificationsRepository;


    @Override
    public Notifications createNotifications(Notifications notification) {
        return notificationsRepository.save(notification);
    }

    @Override
    public List<Notifications> getNotificationByRecipient(ObjectId recipientId) {
        return notificationsRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
    }

    @Override
    public void markNotificationAsRead(ObjectId notificationId) {

        notificationsRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationsRepository.save(notification);
        });
    }

    @Override
    public void markAllAsRead(ObjectId recipientId) {

        List<Notifications> notifications = notificationsRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId);
        for(Notifications notification: notifications){
            if (!notification.isRead()) {
                notification.setRead(true);
                notificationsRepository.save(notification);
            }
        }
    }
}
