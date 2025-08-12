package com.FlightBooking.FlightBookingApplication.Service;

import com.FlightBooking.FlightBookingApplication.Entity.Booking;
import com.FlightBooking.FlightBookingApplication.Entity.Schedule;
import com.FlightBooking.FlightBookingApplication.Entity.Users;
import com.FlightBooking.FlightBookingApplication.Repo.BookingRepository;
import com.FlightBooking.FlightBookingApplication.Repo.ScheduleRepository;
import com.FlightBooking.FlightBookingApplication.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;


    //book a flight for a user on a specific schedule.
    public String bookFlight(long userId, long scheduleId) {
        try {
            Optional<Users> userOpt = userRepository.findById(userId);
            Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
            
            if (userOpt.isEmpty()) {
                return "❌ User not found.";
            }
            if (scheduleOpt.isEmpty()) {
                return "❌ Schedule not found.";
            }

            Schedule schedule = scheduleOpt.get();
            if (schedule.getAvailableSeats() <= 0) {
                return "❌ No available seats for this flight.";
            }
            
            Users user = userOpt.get();
            
            // Create a new booking
            Booking newBooking = new Booking(user, schedule.getFlight(), schedule, LocalDateTime.now());
            bookingRepository.save(newBooking);

            // Update the available seats
            schedule.setAvailableSeats(schedule.getAvailableSeats() - 1);
            scheduleRepository.save(schedule);

            return "✅ Flight booked successfully!";
        } catch (Exception e) {
            return "❌ Error booking flight: " + e.getMessage();
        }
    }
    
    //Retrieves all bookings
    public List<Booking> getUserBookings(Users user) {
        return bookingRepository.findByUser(user);
    }



}
