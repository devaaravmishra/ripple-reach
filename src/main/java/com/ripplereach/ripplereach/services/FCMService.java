package com.ripplereach.ripplereach.services;

public interface FCMService {
    void sendNotification(String targetToken, String title, String body);
}
