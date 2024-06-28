package com.ripplereach.ripplereach.security;

import com.ripplereach.ripplereach.utilities.HashUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        String password = rawPassword.toString();

        if (!HashUtils.verifyHash(password)) {
            return HashUtils.generateHash(password);
        }

        return password;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String password = rawPassword.toString();
        if (!HashUtils.verifyHash(password)) {
            return HashUtils.generateHash(password).equals(encodedPassword);
        }

        return password.equals(encodedPassword);
    }
}