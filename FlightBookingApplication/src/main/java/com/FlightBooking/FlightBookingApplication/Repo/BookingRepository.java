package com.FlightBooking.FlightBookingApplication.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;

import com.FlightBooking.FlightBookingApplication.Entity.Booking;
import com.FlightBooking.FlightBookingApplication.Entity.Users;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings made by a specific user
	
    List<Booking> findByUser(Users user);

    // Find all bookings for a specific flight by its flightId
    List<Booking> findByFlight_FlightId(Long flightId);

}
