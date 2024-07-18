package com.ripplereach.ripplereach.services.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.ripplereach.ripplereach.services.FirebaseAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirebaseAuthServiceImpl implements FirebaseAuthService {

  @Override
  public String verifyIdToken(String idToken) {
    try {
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
      return decodedToken.getUid();
    } catch (FirebaseAuthException e) {
      log.error("Error while verifying user, invalid token!", e);
      throw new AccessDeniedException("Error while verifying user, invalid Token!");
    }
  }
}
