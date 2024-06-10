package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
  @NotBlank(message = Messages.ID_TOKEN_REQUIRED)
  private String idToken;

  @NotBlank(message = Messages.PHONE_REQUIRED)
  @Pattern(regexp = "\\d{10}", message = Messages.VALID_PHONE)
  @Size(message = Messages.INVALID_PHONE, max = 10, min = 10)
  private String phone;
}
