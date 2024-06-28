package com.ripplereach.ripplereach.utilities;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {

  private static final String REGEX = "^[0-9a-fA-F]{64}$";
  public static String generateHash(String value) {
    return new DigestUtils("SHA3-256").digestAsHex(value);
  }
  public static boolean verifyHash(String hash) {
    return hash.matches(REGEX);
  }
}
