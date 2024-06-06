package com.ripplereach.ripplereach.mappers;

import com.ripplereach.ripplereach.dtos.RegisterRequestDto;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(RegisterRequestDto registerRequestDto) {
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());

        if (registerRequestDto.getCompany() != null) {
            Company company = Company.builder()
                    .name(registerRequestDto.getCompany())
                    .build();

            user.setCompany(company);
        }

        else if (registerRequestDto.getUniversity() != null) {
            University university = University.builder()
                    .name(registerRequestDto.getUniversity())
                    .build();

            user.setUniversity(university);
        }

        user.setPhone(registerRequestDto.getPhone());

        return user;
    }
}
