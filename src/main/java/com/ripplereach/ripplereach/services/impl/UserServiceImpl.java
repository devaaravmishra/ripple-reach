package com.ripplereach.ripplereach.services.impl;

import com.ripplereach.ripplereach.exceptions.RippleReachException;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.UserRepository;
import com.ripplereach.ripplereach.services.CompanyService;
import com.ripplereach.ripplereach.services.UniversityService;
import com.ripplereach.ripplereach.services.UserService;
import com.ripplereach.ripplereach.utilities.AvatarGenerator;
import com.ripplereach.ripplereach.utilities.HashUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UniversityService universityService;
    private final CompanyService companyService;

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        try {
            Optional<User> userEntity = getUserByUsername(username);

            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with username {} doesn't exists", username);
                throw new EntityNotFoundException("User with this username doesn't exists!");
            }

            return userEntity.get();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error finding user with username {}", username);
            throw new RuntimeException("Error finding user this username");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByPhone(String phone) {
        try {
            Optional<User> userEntity = getUserByPhone(phone);
            
            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with this phone doesn't exists!");
                throw new EntityNotFoundException("Can't find user with userId: " + phone);
            }
            
            return userEntity.get();
        } catch (RuntimeException ex) {
            log.error("Error while finding user with phone: {}", phone);
            throw new RippleReachException("Error while finding user with phone: " + phone);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUserId(Long userId) {
        try {
            Optional<User> userEntity = getUserByUserId(userId);

            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with userId {} doesn't exists!", userId);
                throw new EntityNotFoundException("Can't find user with userId: " + userId);
            }

            return userEntity.get();
        } catch (EntityNotFoundException ex) {
            throw ex;
        }
    }

    @Override
    @Transactional
    public User create(User user) {
        try {
            String oneWayPhoneHex = generatePhoneHash(user.getPhone());

            // Check if this user already exists or not
            Optional<User> existingUser =
                   getUserByPhone(user.getPhone())
                            .or(() -> userRepository.findUserByUsername(user.getUsername()));

            if (existingUser.isPresent()) {
                log.error("User with phone {} or username {} already exists", oneWayPhoneHex, user.getUsername());
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

            User userEntity = userRepository.save(user);

            log.info("User with id {}, username {} is created successfully.",
                    userEntity.getUserId(),
                    userEntity.getUsername()
            );

            return userEntity;
        } catch (RuntimeException ex) {
            log.error("Error while saving user with username: {}!", user.getUsername());
            throw new RippleReachException("Error while saving user!");
        }
    }

    @Override
    @Transactional
    public User update(Long userId, User user) {
        return saveUserDetails(userId, user);
    }

    @Override
    @Transactional
    public User partialUpdate(Long userId, User user) {
        return saveUserDetails(userId, user);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        try {
            Optional<User> userEntity = getUserByUsername(username);

            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with username {} doesn't exists", username);
                throw new EntityNotFoundException("User with this username doesn't exists!");
            }

            userEntity.get().setDeletedAt(Instant.now());
            User deletedUser = userRepository.save(userEntity.get());

            log.info("User with userId {}, username {} is soft deleted.", deletedUser.getUserId(), deletedUser.getUsername());
        } catch (RuntimeException ex) {
            log.error("Error while deleting user with username {}", username);
            throw new RippleReachException("Error while deleting user!");
        }
    }
    
    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        try {
            Optional<User> userEntity = getUserByUserId(userId);

            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with username {} doesn't exists", userId);
                throw new EntityNotFoundException("User with this username doesn't exists!"); 
            }

            userEntity.get().setDeletedAt(Instant.now());
            User deletedUser = userRepository.save(userEntity.get());

            log.info("User with userId {}, username {} is soft deleted.", deletedUser.getUserId(), deletedUser.getUsername());
        } catch (RuntimeException ex) {
            log.error("Error while deleting user with userId: {}", userId, ex);
            throw new RippleReachException("Error while deleting user!");
        }
    }

    @Override
    @Transactional
    public void deleteByPhone(String phone) {
        String oneWayPhoneHex = generatePhoneHash(phone);

        try {
            Optional<User> userEntity = getUserByPhone(oneWayPhoneHex);

            if (userEntity.isEmpty() || isUserSoftDeleted(userEntity.get())) {
                log.error("User with username {} doesn't exists", oneWayPhoneHex);
                throw new EntityNotFoundException("User with this username doesn't exists!");
            }

            userEntity.get().setDeletedAt(Instant.now());
            User deletedUser = userRepository.save(userEntity.get());

            log.info("User with userId {}, username {} is soft deleted.", deletedUser.getUserId(), deletedUser.getUsername());
        } catch (RuntimeException ex) {
            log.error("Error while deleting user with phone: {}", oneWayPhoneHex, ex);
            throw new RippleReachException("Error while deleting user!");
        }
    }

    private User saveUserDetails(Long userId, User user) {
        try {
            Optional<User> userEntity = getUserByUserId(userId);

            if (userEntity.isEmpty()) {
                log.error("User with userId {} doesn't exist", userId);
                throw new EntityNotFoundException("User with this userId doesn't exist");
            }

            User existingUser = userEntity.get();
            user.setUserId(existingUser.getUserId());

            boolean isUsernamePresent = !user.getUsername().isEmpty();
            boolean isCompanyPresent = user.getCompany() != null;
            boolean isUniversityPresent = user.getUniversity() != null;
            boolean companyAndUniversityPresent = isUniversityPresent && isCompanyPresent;
            boolean isProfessionPresent = !user.getProfession().isEmpty();

            if (isUsernamePresent) {
                existingUser.setUsername(user.getUsername());
            }

            if (companyAndUniversityPresent) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Either university or company must be present, but not both.");
            }

            if (isCompanyPresent) {
                Company company = companyService.create(user.getCompany().getName());
                existingUser.setCompany(company);
            } else if (isUniversityPresent) {
                if (isProfessionPresent) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Unable to update profession along with university.");
                }

                University university = universityService.create(user.getUniversity().getName());
                existingUser.setUniversity(university);
            }

            if (isProfessionPresent) {
                existingUser.setProfession(user.getProfession());
            }

            User updatedUser = userRepository.save(existingUser);

            log.info("User with id {}, username {} is updated successfully.",
                    updatedUser.getUserId(),
                    updatedUser.getUsername()
            );

            return updatedUser;
        } catch (EntityNotFoundException | HttpClientErrorException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Error while updating user!");
            throw new RuntimeException("Error while updating user!", ex);
        }
    }

    private Optional<User> getUserByUserId(Long userId) {
        try {
            return userRepository.findUserByUserId(userId);
        } catch (RuntimeException ex) {
            log.error("Error while finding user with id {}", userId);
            throw new RippleReachException("Error finding user with userId: " + userId);
        }
    }

    private Optional<User> getUserByPhone(String phone) {
        try {
            String oneWayPhoneHex = generatePhoneHash(phone);
            return userRepository.findUserByPhone(oneWayPhoneHex);
        } catch (RuntimeException ex) {
            log.error("Error while finding user with phone {}", phone);
            throw new RippleReachException("Error finding user with phone: " + phone);
        }
    }

    private Optional<User> getUserByUsername(String username) {
        try {
            return userRepository.findUserByUsername(username);
        } catch (RuntimeException ex) {
            log.error("Error while finding user with username {}", username);
            throw new RippleReachException("Error finding user with username: " + username);
        }

    }

    private static String generatePhoneHash(String phone) {
        return HashUtils.generateHash(phone);
    }

    private boolean isUserSoftDeleted(User user) {
        return !user.getDeletedAt().toString().isEmpty();
    }
}
