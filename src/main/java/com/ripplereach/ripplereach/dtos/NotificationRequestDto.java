package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.constants.Messages;
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
    @NotBlank(message = Messages.DEVICE_TOKEN_REQUIRED)
    private String deviceToken;

    @NotBlank(message = Messages.TITLE_REQUIRED)
    private String title;

    @NotBlank(message = Messages.BODY_REQUIRED)
    private String body;
}
