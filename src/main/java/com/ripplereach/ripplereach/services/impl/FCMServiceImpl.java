package com.ripplereach.ripplereach.services.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ripplereach.ripplereach.services.FCMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMServiceImpl implements FCMService {

    @Override
    public void sendNotification(String targetToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("Error while sending notification", e);
        }
    }
}
