package com.ripplereach.ripplereach.mappers;

import com.ripplereach.ripplereach.dtos.RegisterRequestDto;
import com.ripplereach.ripplereach.dtos.UserPartialUpdateRequestDto;
import com.ripplereach.ripplereach.dtos.UserUpdateRequestDto;
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
      Company company = Company.builder().name(registerRequestDto.getCompany()).build();

      user.setCompany(company);
      user.setProfession(registerRequestDto.getProfession());
    } else if (registerRequestDto.getUniversity() != null) {
      University university = University.builder().name(registerRequestDto.getUniversity()).build();

      user.setUniversity(university);
    }

    user.setPhone(registerRequestDto.getPhone());

    return user;
  }

  public User mapToUser(UserPartialUpdateRequestDto userPartialUpdateRequestDto) {
    User user = new User();
    user.setUsername(userPartialUpdateRequestDto.getUsername());

    if (userPartialUpdateRequestDto.getCompany() != null) {
      Company company = Company.builder().name(userPartialUpdateRequestDto.getCompany()).build();

      user.setCompany(company);
      user.setProfession(userPartialUpdateRequestDto.getProfession());
    } else if (userPartialUpdateRequestDto.getUniversity() != null) {
      University university = University.builder().name(userPartialUpdateRequestDto.getUniversity()).build();

      user.setUniversity(university);
    }

    return user;
  }

  public User mapToUser(UserUpdateRequestDto userUpdateRequestDto) {
    User user = new User();
    user.setUsername(userUpdateRequestDto.getUsername());

    if (userUpdateRequestDto.getCompany() != null) {
      Company company = Company.builder().name(userUpdateRequestDto.getCompany()).build();

      user.setCompany(company);
      user.setProfession(userUpdateRequestDto.getProfession());
    } else if (userUpdateRequestDto.getUniversity() != null) {
      University university = University.builder().name(userUpdateRequestDto.getUniversity()).build();

      user.setUniversity(university);
    }

    return user;
  }
}
