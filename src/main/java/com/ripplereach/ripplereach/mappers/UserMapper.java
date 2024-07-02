package com.ripplereach.ripplereach.mappers;

import com.ripplereach.ripplereach.dtos.RegisterRequest;
import com.ripplereach.ripplereach.dtos.UserPartialUpdateRequest;
import com.ripplereach.ripplereach.dtos.UserUpdateRequest;
import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.Role;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

  public User mapToUser(RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());

    if (registerRequest.getCompany() != null) {
      Company company = Company.builder().name(registerRequest.getCompany()).build();

      user.setCompany(company);
      user.setProfession(registerRequest.getProfession());
    } else if (registerRequest.getUniversity() != null) {
      University university = University.builder().name(registerRequest.getUniversity()).build();

      user.setUniversity(university);
    }

    user.setPhone(registerRequest.getPhone());

    if (registerRequest.getRoleName() != null) {
      Role role = Role.builder()
              .name(RoleName.valueOf(registerRequest.getRoleName()))
              .build();

      user.setRoles(Set.of(role));
    }

    return user;
  }

  public User mapToUser(UserPartialUpdateRequest userPartialUpdateRequest) {
    User user = new User();
    user.setUsername(userPartialUpdateRequest.getUsername());

    if (userPartialUpdateRequest.getCompany() != null) {
      Company company = Company.builder().name(userPartialUpdateRequest.getCompany()).build();

      user.setCompany(company);
      user.setProfession(userPartialUpdateRequest.getProfession());
    } else if (userPartialUpdateRequest.getUniversity() != null) {
      University university = University.builder().name(userPartialUpdateRequest.getUniversity()).build();

      user.setUniversity(university);
    }

    return user;
  }

  public User mapToUser(UserUpdateRequest userUpdateRequest) {
    User user = new User();
    user.setUsername(userUpdateRequest.getUsername());

    if (userUpdateRequest.getCompany() != null) {
      Company company = Company.builder().name(userUpdateRequest.getCompany()).build();

      user.setCompany(company);
      user.setProfession(userUpdateRequest.getProfession());
    } else if (userUpdateRequest.getUniversity() != null) {
      University university = University.builder().name(userUpdateRequest.getUniversity()).build();

      user.setUniversity(university);
    }

    return user;
  }
}
