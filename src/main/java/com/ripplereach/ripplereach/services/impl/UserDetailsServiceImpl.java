package com.ripplereach.ripplereach.services.impl;

import static java.util.Collections.singletonList;

import com.ripplereach.ripplereach.models.User;
import com.ripplereach.ripplereach.services.UserService;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String phone) {
    User user = userService.getUserByPhone(phone);
    Set<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toSet());

    return new org.springframework.security.core.userdetails.User(
        user.getPhone(), user.getPhone(), user.getId() != null, true, true, true, authorities);
  }

  private Collection<? extends GrantedAuthority> getAuthorities(String role) {
    return singletonList(new SimpleGrantedAuthority(role));
  }
}
