package com.ripplereach.ripplereach.services;

import java.util.concurrent.CompletableFuture;

public interface FCMService {
    void sendNotification(String targetToken, String title, String body);
    CompletableFuture<Void> sendNotificationAsync(String targetToken, String title, String body);
}
