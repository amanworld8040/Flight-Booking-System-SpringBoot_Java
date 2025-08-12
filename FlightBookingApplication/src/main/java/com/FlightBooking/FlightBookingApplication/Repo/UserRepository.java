package com.FlightBooking.FlightBookingApplication.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FlightBooking.FlightBookingApplication.Entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
	
	// Find a user by their email address,else returning Optional to handle "not found"
    Optional<Users> findByEmail(String email);
    
    // Check if a user exists with a given email
    boolean existsByEmail(String email);
}
