package com.FlightBooking.FlightBookingApplication.Service;

import com.FlightBooking.FlightBookingApplication.Entity.Flight;
import com.FlightBooking.FlightBookingApplication.Entity.Schedule;
import com.FlightBooking.FlightBookingApplication.Repo.FlightRepository;
import com.FlightBooking.FlightBookingApplication.Repo.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FlightRepository flightRepository;
    
    public boolean addSchedule(long flightId, LocalDateTime departure, LocalDateTime arrival) {
        try {
            Optional<Flight> flightOpt = flightRepository.findById(flightId);
            if (flightOpt.isEmpty()) {
                System.out.println("❌ Flight ID not found.");
                return false;
            }

            if (arrival.isBefore(departure)) {
                System.out.println("❌ Arrival time cannot be before departure time.");
                return false;
            }

            Flight flight = flightOpt.get();

            Schedule schedule = new Schedule();
            schedule.setFlight(flight);
            schedule.setDepartureTime(departure);
            schedule.setArrivalTime(arrival);
            schedule.setStatus(Schedule.Status.SCHEDULED);
            schedule.setAvailableSeats(flight.getCapacity());

            scheduleRepository.save(schedule);

            return true;
        } catch (Exception e) {
            System.out.println("❌ Error adding schedule: " + e.getMessage());
            return false;
        }
    }

    public Optional<Schedule> getScheduleById(long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    public List<Schedule> searchSchedules(String origin, String destination, LocalDate departureDate) {
        try {
            LocalDateTime startOfDay = departureDate.atStartOfDay();
            LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);

            System.out.println("\nSearching schedules from " + origin + " to " + destination + "\n");

            List<Schedule> schedules = scheduleRepository
                .findByFlight_OriginIgnoreCaseAndFlight_DestinationIgnoreCaseAndDepartureTimeBetween(
                    origin.trim(), destination.trim(), startOfDay, endOfDay
                );

            if (schedules.isEmpty()) {
                System.out.println("❌ No schedules found.");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                System.out.printf("%-12s %-15s %-15s %-20s %-20s%n",
                        "Flight No", "Origin", "Destination", "Departure Time", "Arrival Time");
                System.out.println("------------------------------------------------------------------------------------");

                for (Schedule s : schedules) {
                    Flight f = s.getFlight();
                    System.out.printf("%-12s %-15s %-15s %-20s %-20s%n",
                            f.getFlightNumber(),
                            f.getOrigin(),
                            f.getDestination(),
                            s.getDepartureTime().format(formatter),
                            s.getArrivalTime().format(formatter)
                    );
                }
            }
            return schedules;
        } catch (Exception e) {
            System.out.println("❌ Error searching schedules: " + e.getMessage());
            return List.of();
        }
    }


    public boolean isCityAvailable(String city) {
        return scheduleRepository.existsByFlight_OriginIgnoreCase(city) ||
               scheduleRepository.existsByFlight_DestinationIgnoreCase(city);
    }
    public boolean isFlightIdValid(Long flightId) {
        return flightRepository.findById(flightId).isPresent();
    }

    public List<Schedule> getSchedulesByOriginDestinationDate(String origin, String destination, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        return scheduleRepository.findByFlight_OriginIgnoreCaseAndFlight_DestinationIgnoreCaseAndDepartureTimeBetween(
            origin, destination, startOfDay, endOfDay);
    }
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    public List<String> getAllOriginCities() {
        return scheduleRepository.findAll().stream()
                .map(s -> s.getFlight().getOrigin())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllDestinationCities() {
        return scheduleRepository.findAll().stream()
                .map(s -> s.getFlight().getDestination())
                .distinct()
                .collect(Collectors.toList());
    }
 
}
