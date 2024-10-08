package com.ripplereach.ripplereach.services.impl;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import com.ripplereach.ripplereach.services.FCMService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMServiceImpl implements FCMService {

  @Override
  public void sendNotification(String targetToken, String title, String body) {
    Notification notification = Notification.builder().setTitle(title).setBody(body).build();

    Message message = Message.builder().setToken(targetToken).setNotification(notification).build();
    try {
      String response = FirebaseMessaging.getInstance().send(message);
      log.info("Successfully sent fcm message: {}", response);
    } catch (FirebaseMessagingException e) {
      log.error("Error while sending notification", e);
    }
  }

  @Override
  @Async
  public CompletableFuture<Void> sendNotificationAsync(
      String targetToken, String title, String body) {
    Notification notification = Notification.builder().setTitle(title).setBody(body).build();

    Message message = Message.builder().setToken(targetToken).setNotification(notification).build();

    ApiFuture<String> response = FirebaseMessaging.getInstance().sendAsync(message);
    return CompletableFuture.runAsync(
        () -> {
          try {
            String result = response.get();
            log.info("Successfully sent FCM message async: {}", result);
          } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending notifications async", e);
          }
        });
  }
}
