package com.FlightBooking.FlightBookingApplication.Repo;

import java.util.List;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FlightBooking.FlightBookingApplication.Entity.Flight;
import com.FlightBooking.FlightBookingApplication.Entity.Schedule;
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Find schedules by flight origin and destination and departure time
    List<Schedule> findByFlight_OriginIgnoreCaseAndFlight_DestinationIgnoreCaseAndDepartureTimeBetween(
        String origin, String destination, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Find schedules for a specific flight with departure time between two datetimes
    List<Schedule> findByFlightAndDepartureTimeBetween(Flight flight, LocalDateTime from, LocalDateTime to);

    // Find schedules by exact flight origin and destination 
    List<Schedule> findByFlight_OriginAndFlight_Destination(String origin, String destination);

    // Check if any schedule exists with the flight origin 
    boolean existsByFlight_OriginIgnoreCase(String origin);

    // Check if any schedule exists with the flight destination 
    boolean existsByFlight_DestinationIgnoreCase(String destination);
   
}
