package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.CompanyAndProfession;
import com.ripplereach.ripplereach.annotations.UniversityOrCompany;
import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@UniversityOrCompany
@CompanyAndProfession
@AllArgsConstructor
@NoArgsConstructor
public class UserPartialUpdateRequest {
  @Size(message = Messages.USERNAME_SIZE, min = 3, max = 25)
  private String username;

  @Size(message = Messages.UNIVERSITY_SIZE, min = 3, max = 50)
  private String university;

  @Size(message = Messages.COMPANY_SIZE, min = 3, max = 50)
  private String company;

  @Size(message = Messages.PROFESSION_SIZE, min = 3, max = 50)
  private String profession;
}
