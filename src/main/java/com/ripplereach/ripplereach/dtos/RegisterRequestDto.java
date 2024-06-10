package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.CompanyAndProfession;
import com.ripplereach.ripplereach.annotations.UniversityOrCompany;
import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@UniversityOrCompany
@CompanyAndProfession
@NoArgsConstructor
public class RegisterRequestDto {
  @NotBlank(message = Messages.ID_TOKEN_REQUIRED)
  private String idToken;

  @NotBlank(message = Messages.USERNAME_REQUIRED)
  private String username;

  @NotBlank(message = Messages.PHONE_REQUIRED)
  @Pattern(regexp = "\\d{10}", message = Messages.VALID_PHONE)
  @Size(message = Messages.INVALID_PHONE, max = 10, min = 10)
  private String phone;

  @Size(message = Messages.COMPANY_SIZE, min = 3, max = 50)
  private String company;

  @Size(message = Messages.UNIVERSITY_SIZE, min = 3, max = 50)
  private String university;

  @Size(message = Messages.PROFESSION_SIZE, min = 3, max = 25)
  private String profession;
}
