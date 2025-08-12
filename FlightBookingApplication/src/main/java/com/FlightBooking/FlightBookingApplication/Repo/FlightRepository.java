package com.FlightBooking.FlightBookingApplication.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.FlightBooking.FlightBookingApplication.Entity.Flight;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT DISTINCT f FROM Flight f LEFT JOIN FETCH f.schedules ORDER BY f.flightId")
    List<Flight> findAllWithSchedules();

    // Finds flights by origin and destination. if it exactly matches
    List<Flight> findByOriginAndDestination(String origin, String destination);

    // Finds a flight by its flight number 
    Optional<Flight> findByFlightNumber(String flightNumber);
    
    // Finds all flights ordered by flightId
    @Query("SELECT f FROM Flight f ORDER BY f.flightId")
    List<Flight> findAllOrderByFlightId();
}
