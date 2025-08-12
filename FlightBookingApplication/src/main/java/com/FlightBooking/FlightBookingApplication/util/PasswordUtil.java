package com.FlightBooking.FlightBookingApplication.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Encrypt the raw password using BCrypt
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    // Check if the raw password matches the encrypted password
    public static boolean match(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
