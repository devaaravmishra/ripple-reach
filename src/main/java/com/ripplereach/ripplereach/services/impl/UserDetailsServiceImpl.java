package com.ripplereach.ripplereach.services.impl;

import static java.util.Collections.singletonList;

import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.repositories.UserRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String phone) {
    String sha3PhoneHex = new DigestUtils("SHA3-256").digestAsHex(phone);
    Optional<User> userOptional = userRepository.findUserByPhone(sha3PhoneHex);
    User user =
        userOptional.orElseThrow(
            () -> new UsernameNotFoundException("No user " + "Found with phone : " + sha3PhoneHex));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPhone(),
        user.getIsVerified(),
        true,
        true,
        true,
        getAuthorities("USER"));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(String role) {
    return singletonList(new SimpleGrantedAuthority(role));
  }
}
