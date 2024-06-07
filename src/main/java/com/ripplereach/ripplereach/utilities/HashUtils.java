package com.ripplereach.ripplereach.utilities;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtils {

  public static String generateHash(String value) {
    return new DigestUtils("SHA3-256").digestAsHex(value);
  }
}
