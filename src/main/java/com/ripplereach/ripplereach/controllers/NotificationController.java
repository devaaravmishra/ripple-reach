package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.NotificationRequestDto;
import com.ripplereach.ripplereach.services.FCMService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {

    private final FCMService fcmService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendNotification(@Valid NotificationRequestDto notificationRequest) {
        fcmService.sendNotification(notificationRequest.getDeviceToken(), notificationRequest.getTitle(), notificationRequest.getBody());
    }
}
