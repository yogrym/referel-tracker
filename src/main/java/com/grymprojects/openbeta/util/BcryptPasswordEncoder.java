package com.grymprojects.openbeta.util;

public class BcryptPasswordEncoder {

    private static final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder ENCODER =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    private BcryptPasswordEncoder() {
    }

    public static String encodePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }

        return ENCODER.encode(password);
    }

    public static boolean matches(String password, String encodedPassword) {
        if (password == null || password.isBlank() || encodedPassword == null || encodedPassword.isBlank()) {
            return false;
        }

        return ENCODER.matches(password, encodedPassword);
    }
}
