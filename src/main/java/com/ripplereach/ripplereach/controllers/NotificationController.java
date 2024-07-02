package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.NotificationRequest;
import com.ripplereach.ripplereach.services.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "The Notification API. Contains all the operations that can be performed on a notification.")
@AllArgsConstructor
public class NotificationController {

    private final FCMService fcmService;

    @PostMapping("/send")
    @SecurityRequirement(name = "Bearer Authentication")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Send Notification",
            description = "Sends a push notification via FCM."
    )
    public void sendNotification(@Valid NotificationRequest notificationRequest) {
        fcmService.sendNotification(
                notificationRequest.getDeviceToken(),
                notificationRequest.getTitle(),
                notificationRequest.getBody()
        );
    }
}
