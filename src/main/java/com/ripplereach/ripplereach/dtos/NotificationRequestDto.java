package com.ripplereach.ripplereach.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    @NotBlank(message = "Device token is required.")
    private String deviceToken;

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Body is required.")
    private String body;
}
