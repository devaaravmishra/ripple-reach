package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.CompanyAndProfession;
import com.ripplereach.ripplereach.annotations.UniversityOrCompany;
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
public class UserPartialUpdateRequestDto {
    @Size(message = "username must be between 3 to 25 characters", min = 3, max = 25)
    private String username;

    @Size(message = "university name length must be between 3 to 30 characters", min = 3, max = 30)
    private String university;

    @Size(message = "company name length must be between 3 to 30 characters", min = 3, max = 30)
    private String company;

    @Size(message = "profession name length must be between 3 to 20 characters", min = 3, max = 20)
    private String profession;
}
