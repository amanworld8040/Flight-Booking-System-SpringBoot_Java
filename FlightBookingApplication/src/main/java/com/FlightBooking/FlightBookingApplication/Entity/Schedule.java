package com.FlightBooking.FlightBookingApplication.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

  
    private LocalDateTime departureTime;

    
    private LocalDateTime arrivalTime;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private int availableSeats;

    public enum Status {
        SCHEDULED, CANCELLED, DELAYED
    }

    public Schedule() {}

    public Schedule(Flight flight, LocalDateTime departureTime, LocalDateTime arrivalTime, Status status, int availableSeats) {
        this.flight = flight;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.availableSeats = availableSeats;
    }

    // Getters and setters

    public Long getScheduleId() {
        return scheduleId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
