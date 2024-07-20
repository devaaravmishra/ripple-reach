package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.dtos.UserResponse;
import com.ripplereach.ripplereach.enums.RoleName;
import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.Role;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.UserRepository;
import com.ripplereach.ripplereach.services.CompanyService;
import com.ripplereach.ripplereach.services.RoleService;
import com.ripplereach.ripplereach.services.UniversityService;
import com.ripplereach.ripplereach.services.UserService;
import com.ripplereach.ripplereach.utilities.AvatarGenerator;
import com.ripplereach.ripplereach.utilities.HashUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UniversityService universityService;
  private final CompanyService companyService;
  private final RoleService roleService;
  private final Mapper<User, UserResponse> userResponseMapper;

  @Override
  @Transactional(readOnly = true)
  public UserResponse findByUsername(String username) {
    User user = getUserByUsername(username);

    return userResponseMapper.mapTo(user);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  @Override
  @Transactional(readOnly = true)
  public Set<String> findExistingUsernames(Set<String> usernames) {
    try {
      return userRepository.findExistingUsernames(usernames);
    } catch (RuntimeException ex) {
      log.error("Error occurred while fetching an list of usernames!", ex);
      throw new RippleReachException("Error occurred while fetching an list of usernames!");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findByPhone(String phone) {
    User user = getUserByPhone(phone);

    return userResponseMapper.mapTo(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponse findById(Long userId) {
    User user = getUserById(userId);

    return userResponseMapper.mapTo(user);
  }

  @Override
  @Transactional
  public UserResponse create(User user) {
    try {
      String oneWayPhoneHex = generatePhoneHash(user.getPhone());

      // Check if this user already exists or not
      Optional<User> existingUser =
          userRepository
              .findByPhone(oneWayPhoneHex)
              .or(() -> userRepository.findByUsername(user.getUsername()));

      if (existingUser.isPresent()) {
        log.error(
            "User with phone {} or username {} already exists", oneWayPhoneHex, user.getUsername());

        throw new EntityExistsException("User with these credentials already exists");
      }

      user.setPhone(oneWayPhoneHex);

      if (user.getCompany() != null) {
        Company company = companyService.create(user.getCompany().getName());
        user.setCompany(company);

        // Set the user's profession if he/she is working somewhere
        user.setProfession(user.getProfession());
      } else if (user.getUniversity() != null) {
        University university = universityService.create(user.getUniversity().getName());
        user.setUniversity(university);
      }

      user.setIsVerified(false);

      String avatar = AvatarGenerator.generateRandomAvatar();
      user.setAvatar(avatar);

      setRoles(user);

      User userEntity = userRepository.save(user);

      log.info(
          "User with id {}, username {} is created successfully.",
          userEntity.getId(),
          userEntity.getUsername());

      return userResponseMapper.mapTo(userEntity);
    } catch (EntityExistsException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while saving user with username: {}!", user.getUsername());
      throw new RippleReachException(
          "Error while saving user with username: " + user.getUsername());
    }
  }

  @Override
  @Transactional
  public UserResponse update(Long userId, User user) {
    return saveUserDetails(userId, user);
  }

  @Override
  @Transactional
  public UserResponse partialUpdate(Long userId, User user) {
    return saveUserDetails(userId, user);
  }

  @Override
  @Transactional
  public void deleteByUsername(String username, boolean hardDelete) {
    try {
      User userEntity = getUserByUsername(username);

      if (hardDelete) {
        userRepository.delete(userEntity);
        log.info(
            "User with userId {}, username {} is hard deleted.",
            userEntity.getId(),
            userEntity.getUsername());
        return;
      }

      userEntity.setDeletedAt(Instant.now());
      User deletedUser = userRepository.save(userEntity);

      log.info(
          "User with userId {}, username {} is soft deleted.",
          deletedUser.getId(),
          deletedUser.getUsername());
    } catch (RuntimeException ex) {
      log.error("Error while deleting user with username {}", username);
      throw new RippleReachException("Error while deleting user!");
    }
  }

  @Override
  @Transactional
  public void deleteById(Long userId, boolean hardDelete) {
    try {
      User userEntity = getUserById(userId);

      if (hardDelete) {
        userRepository.delete(userEntity);
        log.info(
            "User with userId {}, username {} is hard deleted.",
            userEntity.getId(),
            userEntity.getUsername());
        return;
      }

      userEntity.setDeletedAt(Instant.now());
      User deletedUser = userRepository.save(userEntity);

      log.info(
          "User with userId {}, username {} is soft deleted.",
          deletedUser.getId(),
          deletedUser.getUsername());
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while deleting user with userId: {}", userId, ex);
      throw new RippleReachException("Error while deleting user!");
    }
  }

  @Override
  @Transactional
  public void deleteByPhone(String phone, boolean hardDelete) {
    try {
      User userEntity = getUserByPhone(phone);

      if (hardDelete) {
        userRepository.delete(userEntity);
        log.info(
            "User with userId {}, username {} is hard deleted.",
            userEntity.getId(),
            userEntity.getUsername());
        return;
      }

      userEntity.setDeletedAt(Instant.now());
      User deletedUser = userRepository.save(userEntity);

      log.info(
          "User with userId {}, username {} is soft deleted.",
          deletedUser.getId(),
          deletedUser.getUsername());
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while deleting user with phone: {}", phone, ex);
      throw new RippleReachException("Error while deleting user!");
    }
  }

  private UserResponse saveUserDetails(Long userId, User user) {
    try {
      User existingUser = getUserById(userId);

      user.setId(existingUser.getId());

      boolean isUsernamePresent = !user.getUsername().isEmpty();
      boolean isCompanyPresent = user.getCompany() != null;
      boolean isUniversityPresent = user.getUniversity() != null;
      boolean companyAndUniversityPresent = isUniversityPresent && isCompanyPresent;
      boolean isProfessionPresent = !user.getProfession().isEmpty();

      if (isUsernamePresent) {
        existingUser.setUsername(user.getUsername());
      }

      if (companyAndUniversityPresent) {
        throw new HttpClientErrorException(
            HttpStatus.BAD_REQUEST, "Either university or company must be present, but not both.");
      }

      if (isCompanyPresent) {
        Company company = companyService.create(user.getCompany().getName());
        existingUser.setCompany(company);
      } else if (isUniversityPresent) {
        if (isProfessionPresent) {
          throw new HttpClientErrorException(
              HttpStatus.BAD_REQUEST, "Unable to update profession along with university.");
        }

        University university = universityService.create(user.getUniversity().getName());
        existingUser.setUniversity(university);
      }

      if (isProfessionPresent) {
        existingUser.setProfession(user.getProfession());
      }

      User updatedUser = userRepository.save(existingUser);

      log.info(
          "User with id {}, username {} is updated successfully.",
          updatedUser.getId(),
          updatedUser.getUsername());

      return userResponseMapper.mapTo(updatedUser);
    } catch (EntityNotFoundException | HttpClientErrorException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while updating user!");
      throw new RuntimeException("Error while updating user!", ex);
    }
  }

  public User getUserById(Long userId) {
    try {
      Optional<User> userEntity = userRepository.findById(userId);

      if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
        log.error("User with userId {} doesn't exists!", userId);
        throw new EntityNotFoundException("Can't find user with userId: " + userId);
      }

      return userEntity.get();
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while finding user with id {}", userId);
      throw new RippleReachException("Error finding user with userId: " + userId);
    }
  }

  public User getUserByPhone(String phone) {
    try {
      if (!HashUtils.verifyHash(phone)) {
        phone = generatePhoneHash(phone);
      }

      Optional<User> userEntity = userRepository.findByPhone(phone);
      if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
        log.error("User with this phone doesn't exists!");
        throw new EntityNotFoundException("Can't find user with phone: " + phone);
      }

      return userEntity.get();
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while finding user with requested phone: {}", phone, ex);
      throw new RippleReachException("Error finding user with required phone: " + phone);
    }
  }

  public User getUserByUsername(String username) {
    try {
      Optional<User> userEntity = userRepository.findByUsername(username);

      if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
        log.error("User with this username: {} doesn't exists!", username);
        throw new EntityNotFoundException("Can't find user with username: " + username);
      }

      return userEntity.get();
    } catch (EntityNotFoundException ex) {
      throw ex;
    } catch (RuntimeException ex) {
      log.error("Error while finding user with username {}", username);
      throw new RippleReachException("Error finding user with username: " + username);
    }
  }

  private void setRoles(User user) {
    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
      Set<Role> roles =
          user.getRoles().stream()
              .map(role -> roleService.createRoleIfNotExists(role.getName()))
              .collect(Collectors.toSet());
      user.setRoles(roles);

      return;
    }

    Set<Role> roles = Collections.singleton(roleService.createRole(RoleName.USER));

    user.setRoles(roles);
  }

  private static String generatePhoneHash(String phone) {
    return HashUtils.generateHash(phone);
  }

  private boolean isUserSoftDeleted(User user) {
    return user.getDeletedAt() != null;
  }
}
