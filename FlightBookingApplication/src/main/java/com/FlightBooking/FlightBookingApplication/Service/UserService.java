package com.FlightBooking.FlightBookingApplication.Service;

import com.FlightBooking.FlightBookingApplication.Entity.Users;
import com.FlightBooking.FlightBookingApplication.Repo.UserRepository;
import com.FlightBooking.FlightBookingApplication.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String signup(String name, String email, String password, String phone, String roleInput) {
        try {
            // Basic validation
            if (name == null || name.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank() ||
                phone == null || phone.isBlank() ||
                roleInput == null || roleInput.isBlank()) {
                return "❌ All fields are required.";
            }

            // Email already exists?
            if (userRepository.existsByEmail(email.trim())) {
                return "❌ Email already exists. Try logging in.";
            }

            // Convert and validate role
            Users.Role role;
            try {
                role = Users.Role.valueOf(roleInput.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return "❌ Invalid role. Allowed values: ADMIN or CUSTOMER.";
            }

            // Encrypt password
            String encryptedPassword = PasswordUtil.encrypt(password.trim());

            // Create and save user
            Users newUser = new Users(
                    name.trim(),
                    encryptedPassword,
                    email.trim(),
                    phone.trim(),
                    role
            );

            userRepository.save(newUser);

            return "User registered successfully! You can now login.";

        } catch (Exception e) {
            return "❌ Error occurred during signup: " + e.getMessage();
        }
    }

    public Users login(String email, String password) {
        try {
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                System.out.println("❌ Email and password cannot be empty.");
                return null;
            }

            Optional<Users> optionalUser = userRepository.findByEmail(email.trim());

            if (optionalUser.isEmpty()) {
                System.out.println("❌ No user found with that email.");
                return null;
            }

            Users user = optionalUser.get();

            if (!PasswordUtil.match(password, user.getPassword())) {
                System.out.println("❌ Incorrect password.");
                return null;
            }

            return user;

        } catch (Exception e) {
            System.out.println("❌ Error during login: " + e.getMessage());
            return null;
        }
    }
}
