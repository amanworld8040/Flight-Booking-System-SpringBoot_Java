package com.FlightBooking.FlightBookingApplication.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;  // Unique ID for each booking, auto-generated

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; 

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;  

    @Column(nullable = false)
    private LocalDateTime bookingDate; 

    //default constructor needed by JPA
    public Booking() {}

    // Constructor 
    public Booking(Users user, Flight flight, Schedule schedule, LocalDateTime bookingDate) {
        this.user = user;
        this.flight = flight;
        this.schedule = schedule;
        this.bookingDate = bookingDate;
    }

    // Getters and setters

    public Long getBookingId() {
        return bookingId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }
}
