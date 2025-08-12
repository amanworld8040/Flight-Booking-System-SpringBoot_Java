package com.FlightBooking.FlightBookingApplication.Service;

import com.FlightBooking.FlightBookingApplication.Entity.Flight;
import com.FlightBooking.FlightBookingApplication.Repo.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    // Add new flight
    @Transactional
    public void addFlight(String flightNumber, String airline, String origin, String destination, int capacity) {
        try {
            if (flightRepository.findByFlightNumber(flightNumber).isPresent()) {
                System.out.println("⚠️ Flight number already exists. Use a different flight number.");
                return;
            }
            if (capacity <= 0) {
                System.out.println("❌ Capacity must be greater than 0.");
                return;
            }

            Flight flight = new Flight(flightNumber, airline, origin, destination, capacity);
            flightRepository.save(flight);
            System.out.println("Flight added successfully.");
        } catch (Exception e) {
            System.out.println("❌ Error adding flight: " + e.getMessage());
        }
    }

    // Retrieve all flights with schedules 
    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        try {
            return flightRepository.findAllWithSchedules();
        } catch (Exception e) {
            System.out.println("❌ Error retrieving flights: " + e.getMessage());
            return List.of();
        }
    }

    // Search flights by origin and destination
    @Transactional(readOnly = true)
    public List<Flight> searchFlights(String from, String to) {
        try {
            List<Flight> flights = flightRepository.findByOriginAndDestination(from, to);
            if (flights.isEmpty()) {
                System.out.println("⚠️ No flights found for this route.");
            }
            return flights;
        } catch (Exception e) {
            System.out.println("❌ Error searching for flights: " + e.getMessage());
            return List.of();
        }
    }

    // Get flight by ID
    @Transactional(readOnly = true)
    public Optional<Flight> getFlightById(long flightId) {
        try {
            return flightRepository.findById(flightId);
        } catch (Exception e) {
            System.out.println("❌ Error retrieving flight: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Get flight by flight number
    @Transactional(readOnly = true)
    public Optional<Flight> getFlightByFlightNumber(String flightNumber) {
        try {
            return flightRepository.findByFlightNumber(flightNumber);
        } catch (Exception e) {
            System.out.println("❌ Error retrieving flight by number: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Update entire flight details
    @Transactional
    public boolean updateFlight(long flightId, String flightNumber, String airline, String origin, String destination, int capacity) {
        try {
            Optional<Flight> optionalFlight = flightRepository.findById(flightId);
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                flight.setFlightNumber(flightNumber);
                flight.setAirline(airline);
                flight.setOrigin(origin);
                flight.setDestination(destination);
                flight.setCapacity(capacity);
                flightRepository.save(flight);
                System.out.println("Flight updated successfully.");
                return true;
            } else {
                System.out.println("X Flight with ID " + flightId + " not found.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("X Error updating flight: " + e.getMessage());
            return false;
        }
    }

    // Update only flight capacity
    @Transactional
    public boolean updateCapacity(long flightId, int newCapacity) {
        try {
            if (newCapacity <= 0) {
                System.out.println("❌ Capacity must be greater than 0.");
                return false;
            }

            Optional<Flight> optionalFlight = flightRepository.findById(flightId);
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                flight.setCapacity(newCapacity);
                flightRepository.save(flight);
                System.out.println("Flight capacity updated successfully.");
                return true;
            } else {
                System.out.println("X Flight with ID " + flightId + " not found.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("X Error updating capacity: " + e.getMessage());
            return false;
        }
    }

    // Delete flight by ID
    @Transactional
    public void deleteFlightById(Long id) {
        try {
            if (!flightRepository.existsById(id)) {
                System.out.println("Flight with ID " + id + " does not exist.");
                return;
            }
            flightRepository.deleteById(id);
            System.out.println("Flight deleted successfully.");
        } catch (Exception e) {
            System.out.println("X Error deleting flight: " + e.getMessage());
        }
    }

    // View all flights - static method, for display in table format
    @Transactional(readOnly = true)
    public static void viewAllFlights(FlightService flightService) {
        List<Flight> flights = flightService.getAllFlights();
        if (flights.isEmpty()) {
            System.out.println("No flights found in the system.");
            return;
        }

        System.out.println("===== ALL FLIGHTS =====");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s %-10s%n",
                "FlightID", "FlightNumber", "Airline", "Origin", "Destination", "Capacity");
        System.out.println("--------------------------------------------------------------------------");

        for (Flight flight : flights) {
            System.out.printf("%-10d %-15s %-15s %-15s %-15s %-10d%n",
                    flight.getFlightId(),
                    flight.getFlightNumber(),
                    flight.getAirline(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getCapacity());
        }
    }

    // Save a flight directly (used for saving updated entities)
    @Transactional
    public Flight saveFlight(Flight flight) {
        try {
            return flightRepository.save(flight);
        } catch (Exception e) {
            throw new RuntimeException("X Failed to save flight: " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    public List<Flight> getAllFlightsOrdered() {
        try {
            return flightRepository.findAllOrderByFlightId();
        } catch (Exception e) {
            System.out.println("X Error retrieving ordered flights: " + e.getMessage());
            return List.of();
        }
    }

}
