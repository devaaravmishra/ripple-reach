package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.CompanyAndProfession;
import com.ripplereach.ripplereach.annotations.UniversityOrCompany;
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
  @NotBlank(message = "ID token is required")
  private String idToken;

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Phone is required")
  @Pattern(regexp = "\\d{10}", message = "Phone number must be digits only")
  @Size(message = "Invalid phone number", max = 10, min = 10)
  private String phone;

  @Size(message = "Company length must be between 3 to 50 characters", min = 3, max = 50)
  private String company;

  @Size(message = "University length must be between 3 to 50 characters", min = 3, max = 50)
  private String university;

  @Size(message = "Profession length must be between 3 to 25 characters", min = 3, max = 25)
  private String profession;
}
