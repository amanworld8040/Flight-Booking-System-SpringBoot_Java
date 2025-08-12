package com.FlightBooking.FlightBookingApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.FlightBooking.FlightBookingApplication.Console.ConsoleApp;

@SpringBootApplication
public class FlightBookingApplication {

    public static void main(String[] args) {
    	
    	// Starts the app and gives access services
        ConfigurableApplicationContext context = SpringApplication.run(FlightBookingApplication.class, args);

        // Instantiate and run the new ConsoleApp
        ConsoleApp consoleApp = new ConsoleApp(context);
        consoleApp.run();
    }
}