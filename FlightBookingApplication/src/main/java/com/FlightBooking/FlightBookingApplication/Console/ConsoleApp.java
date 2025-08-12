package com.FlightBooking.FlightBookingApplication.Console;

import com.FlightBooking.FlightBookingApplication.Entity.Booking;
import com.FlightBooking.FlightBookingApplication.Entity.Flight;
import com.FlightBooking.FlightBookingApplication.Entity.Schedule;
import com.FlightBooking.FlightBookingApplication.Entity.Users;
import com.FlightBooking.FlightBookingApplication.Service.BookingService;
import com.FlightBooking.FlightBookingApplication.Service.FlightService;
import com.FlightBooking.FlightBookingApplication.Service.ScheduleService;
import com.FlightBooking.FlightBookingApplication.Service.UserService;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApp {

    private final UserService userService;
    private final FlightService flightService;
    private final ScheduleService scheduleService;
    private final BookingService bookingService;
    private final Scanner scanner;
    private final ConfigurableApplicationContext context;

    public ConsoleApp(ConfigurableApplicationContext context) {
        this.context = context;
        // Fetch required service beans from the application context
        this.userService = context.getBean(UserService.class);
        this.flightService = context.getBean(FlightService.class);
        this.scheduleService = context.getBean(ScheduleService.class);
        this.bookingService = context.getBean(BookingService.class);
        this.scanner = new Scanner(System.in);
    }
 
    public void run() {
        while (true) {
            System.out.println("\n===== FLIGHT BOOKING SYSTEM =====");
            System.out.println("1. Flight Search");
            System.out.println("2. Login");
            System.out.println("3. Signup");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            System.out.flush();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    flightSearch();
                    break;
                case "2":
                    Users user = login();
                    if (user != null) {
                        if (user.getRole() == Users.Role.ADMIN) {
                            showAdminMenu();
                        } else {
                            showCustomerMenu(user);
                        }
                    }
                    break;
                case "3":
                    signup();
                    break;
                case "4":
                    System.out.println("Exiting the system. Goodbye!");
                    context.close();
                    return;
                default:
                    System.out.println("X Invalid option. Try again.");
            }
        }
    }

    private void flightSearch() {
        System.out.println("\n===== FLIGHT SEARCH =====");

        String from;
        while (true) {
            System.out.println();
            System.out.print("From (City): ");
            System.out.flush();
            from = scanner.nextLine().trim();
            if (!scheduleService.isCityAvailable(from)) {
                System.out.println("X CITY NOT FOUND. RE-ENTER.");
            } else {
                break;
            }
        }
        
        String to;
        while (true) {
            System.out.print("To (City): ");
            System.out.flush();
            to = scanner.nextLine().trim();
            if (!scheduleService.isCityAvailable(to)) {
                System.out.println("X CITY NOT FOUND. RE-ENTER.");
            } else {
                break;
            }
        }

        LocalDate departureDate;
        while (true) {
            System.out.print("Departure Date (yyyy-MM-dd): ");
            System.out.flush();
            String dateInput = scanner.nextLine().trim();
            try {
                departureDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            } catch (DateTimeParseException e) {
                System.out.println("X Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        List<Schedule> foundSchedules = scheduleService.searchSchedules(from, to, departureDate);
        foundSchedules.sort(Comparator.comparing(Schedule::getScheduleId));
        printSchedulesTable(foundSchedules);
    }
	private void printSchedulesTable(List<Schedule> foundSchedules) {
	}
	private Users login() {
        
    	System.out.println("\n===== LOGIN =====");

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();  // Get email and remove extra spaces
            if (!isValidEmail(email)) {
                System.out.println("❌ Invalid email format. Please enter a valid email.");
                continue;
            }
            break;
        }

        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine().trim();  // Get password and trim spaces
            if (password.isEmpty()) {
                System.out.println("X Password cannot be empty. Please enter your password.");
                continue;
            } else if (password.length() < 5) {
                System.out.println("X Password must be at least 5 characters long.");
                continue;
            }
            break;
        }

        Users user = userService.login(email, password);
        if (user != null) {
            System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
        }

        return user;
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    private void signup() {
        System.out.println("\n===== SIGNUP =====");

        String name;
        while (true) {
            System.out.print("Enter full name: ");
            name = scanner.nextLine().trim();
            if (!name.matches("^[a-zA-Z ]+$")) {
                System.out.println("X Name must contain only letters and spaces. Please try again.");
                continue;
            }
            break;
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();
            if (!isValidEmail(email)) {
                System.out.println("X Invalid email format. Please enter a valid email.");
                continue;
            }
            break;
        }

        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("X Password cannot be empty. Please enter your password.");
                continue;
            } else if (password.length() < 5) {
                System.out.println("X Password must be at least 5 characters long.");
                continue;
            }
            break;
        }
        
        String phone;
        while (true) {
            System.out.print("Enter phone number: ");
            phone = scanner.nextLine().trim();
            if (!phone.matches("\\d+")) {
                System.out.println("X Phone number must contain digits only. Please try again.");
                continue;
            }
            break;
        }


        String role;
        while (true) {
            System.out.print("Enter role (ADMIN/CUSTOMER): ");
            role = scanner.nextLine().trim().toUpperCase();
            if (!role.equals("ADMIN") && !role.equals("CUSTOMER")) {
                System.out.println("X Role must be either 'ADMIN' or 'CUSTOMER'. Please try again.");
                continue;
            }
            break;
        }

        String result = userService.signup(name, email, password, phone, role);
        System.out.println(result);
    }
    
    private void showAdminMenu() {
        while (true) {
            System.out.println("===== ADMIN MENU =====");
            System.out.println("1. Add Flight");
            System.out.println("2. Add Schedule");
            System.out.println("3. View All Flights");
            System.out.println("4. Update Flight");
            System.out.println("5. Delete Flight");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addFlight();
                    break;

                case "2":
                    addSchedule();
                    break;

                case "3":
                    viewAllFlights();
                    break;

                case "4":
                    updateFlight();
                    break;

                case "5":
                    deleteFlight();
                    break;

                case "6":
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("X Invalid option. Please try again.");
            }
        }
    }
    
    private void deleteFlight() {
        System.out.print("Enter Flight ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<Flight> optionalFlight = flightService.getFlightById(id);
        if (optionalFlight.isEmpty()) {
            System.out.println("X Flight not found.");
            return;
        }

        flightService.deleteFlightById(id);
        System.out.println(" Flight deleted successfully!");
    }
   
    private void updateFlight() {
        System.out.print("Enter Flight ID to update: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("X Invalid flight ID. Please enter a valid number.");
            return;
        }

        Optional<Flight> optionalFlight = flightService.getFlightById(id);
        if (optionalFlight.isEmpty()) {
            System.out.println("X Flight not found with ID: " + id);
            return;
        }

        Flight flight = optionalFlight.get();

        System.out.print("Enter new flight number (" + flight.getFlightNumber() + "): ");
        String number = scanner.nextLine().trim();
        if (!number.isEmpty()) {
            Optional<Flight> existing = flightService.getFlightByFlightNumber(number);
            if (existing.isPresent() && !existing.get().getFlightId().equals(flight.getFlightId())) {
                System.out.println("X Flight number already exists. Please enter a unique flight number.");
                return;
            }
            flight.setFlightNumber(number);
            System.out.println("Flight number updated.");
        } else {
            System.out.println("Keeping existing flight number.");
        }

        System.out.print("Enter new airline (" + flight.getAirline() + "): ");
        String airline = scanner.nextLine().trim();
        if (!airline.isEmpty()) {
            flight.setAirline(airline);
            System.out.println(" Airline updated.");
        } else {
            System.out.println("Keeping existing airline.");
        }

        System.out.print("Enter new origin (" + flight.getOrigin() + "): ");
        String origin = scanner.nextLine().trim();
        if (!origin.isEmpty()) {
            flight.setOrigin(origin);
            System.out.println("Origin updated.");
        } else {
            System.out.println("Keeping existing origin.");
        }

        System.out.print(" Enter new destination (" + flight.getDestination() + "): ");
        String destination = scanner.nextLine().trim();
        if (!destination.isEmpty()) {
            flight.setDestination(destination);
            System.out.println("Destination updated.");
        } else {
            System.out.println("Keeping existing destination.");
        }

        System.out.print(" Enter new capacity (" + flight.getCapacity() + "): ");
        String capacityStr = scanner.nextLine().trim();
        if (!capacityStr.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    System.out.println("Capacity must be greater than 0. Skipping capacity update.");
                } else {
                    flight.setCapacity(capacity);
                    System.out.println("Capacity updated.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity input. Skipping capacity update.");
            }
        } else {
            System.out.println("Keeping existing capacity.");
        }

        try {
            flightService.saveFlight(flight);
            System.out.println("\nFlight updated successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error while updating flight: " + e.getMessage());
        }
    }

    public void viewAllFlights() {
        List<Flight> flights = flightService.getAllFlightsOrdered();  
        if (flights.isEmpty()) {
            System.out.println(" No flights found in the system.");
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

    private void addFlight() {
        try {
            System.out.println("===== ADD FLIGHT =====");

            System.out.print("Enter flight number: ");
            String flightNumber = scanner.nextLine().trim();

            System.out.print("Enter airline name: ");
            String airline = scanner.nextLine().trim();

            System.out.print("Enter origin: ");
            String origin = scanner.nextLine().trim();

            System.out.print("Enter destination: ");
            String destination = scanner.nextLine().trim();

            System.out.print("Enter total capacity (number of seats): ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());  

            flightService.addFlight(flightNumber, airline, origin, destination, capacity);

            System.out.println(" Flight added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("X Invalid number format for capacity.");
        } catch (Exception e) {
            System.out.println("X Error adding flight: " + e.getMessage());
        }
    }
 
    private void addSchedule() {
        try {
            System.out.println("===== ADD SCHEDULE =====");
            long flightId = -1;
            while (true) {
                System.out.print("Enter flight ID (or type 'cancel' to exit): ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("cancel")) {
                    System.out.println("X Schedule creation cancelled.");
                    return;
                }

                try {
                    flightId = Long.parseLong(input);
                    if (!scheduleService.isFlightIdValid(flightId)) {
                        System.out.println("X No flight found with ID " + flightId + ". Please try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("X Invalid number format. Please enter a valid flight ID.");
                }
            }

            LocalDateTime departureTime = readDateTime(scanner, "Enter departure datetime (yyyy-MM-dd HH:mm): ");
            LocalDateTime arrivalTime = readDateTime(scanner, "Enter arrival datetime (yyyy-MM-dd HH:mm): ");

            if (!arrivalTime.isAfter(departureTime)) {
                System.out.println("X Arrival time must be after departure time (plane departs first, then arrives).");
                return;
            }

            boolean success = scheduleService.addSchedule(flightId, departureTime, arrivalTime);
            if (success) {
                System.out.println("Schedule added successfully!");
            }
        } catch (Exception e) {
            System.out.println("X Error adding schedule: " + e.getMessage());
        }
    }
    
    private void showCustomerMenu(Users user) {
        while (true) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("Welcome, " + user.getUsername() + "!");

            System.out.println("1. View Available Flights");
            System.out.println("2. Book a Flight");
            System.out.println("3. View My Bookings");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAllAvailableFlights();
                    break;

                case "2":
                    bookFlight(user);
                    break;

                case "3":
                    viewMyBookings(user);
                    break;

                case "4":
                    System.out.println(" Logging out...");
                    return;

                default:
                    System.out.println("X Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllAvailableFlights() {
        List<Schedule> schedules = scheduleService.getAllSchedules();

        if (schedules.isEmpty()) {
            System.out.println(" No flight schedules available.");
            return;
        }

        schedules.sort(Comparator.comparingLong(Schedule::getScheduleId));

        System.out.println("\n=============================================== AVAILABLE FLIGHTS ===============================================================");
        System.out.printf("%-12s %-15s %-15s %-15s %-15s %-20s %-20s %-10s%n",
                "Schedule ID", "Flight No", "Airline", "From", "To", "Departure", "Arrival", "Seats Left");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Schedule schedule : schedules) {
            if (schedule.getAvailableSeats() > 0) {
                System.out.printf("%-12d %-15s %-15s %-15s %-15s %-20s %-20s %-10d%n",
                        schedule.getScheduleId(),
                        schedule.getFlight().getFlightNumber(),
                        schedule.getFlight().getAirline(),
                        schedule.getFlight().getOrigin(),
                        schedule.getFlight().getDestination(),
                        schedule.getDepartureTime().format(formatter),
                        schedule.getArrivalTime().format(formatter),
                        schedule.getAvailableSeats());
            }
        }

        System.out.println("=================================================================================================================================");
    }

    private void bookFlight(Users user) {
        try {
            System.out.println("\n===== BOOK A FLIGHT =====");

            String origin;
            while (true) {
                System.out.print("From (Origin City): ");
                origin = scanner.nextLine().trim();

                if (origin.isEmpty()) {
                    System.out.println("X Origin city cannot be empty. Please try again.");
                    continue;
                }

                if (!scheduleService.isCityAvailable(origin)) {
                    System.out.println("X CITY NOT FOUND. RE-ENTER.");
                    continue;
                }

                break;
            }

            String destination;
            while (true) {
                System.out.print("To (Destination City): ");
                destination = scanner.nextLine().trim();

                if (destination.isEmpty()) {
                    System.out.println("X Destination city cannot be empty. Please try again.");
                    continue;
                }

                if (!scheduleService.isCityAvailable(destination)) {
                    System.out.println("X CITY NOT FOUND. RE-ENTER.");
                    continue;
                }

                break;
            }

            String departureDate;
            while (true) {
                System.out.print("Departure Date (yyyy-MM-dd): ");
                departureDate = scanner.nextLine().trim();

                if (departureDate.isEmpty()) {
                    System.out.println("X Date cannot be empty. Please enter a valid date.");
                    continue;
                }

                try {
                    LocalDate.parse(departureDate);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("X Invalid date format. Please use yyyy-MM-dd.");
                }
            }

            List<Schedule> matchingSchedules = scheduleService.getSchedulesByOriginDestinationDate(origin, destination, departureDate);

            if (matchingSchedules.isEmpty()) {
                System.out.println("X No flights found for the given route and date.");
                return;
            }

            matchingSchedules.sort(Comparator.comparingLong(Schedule::getScheduleId));

            System.out.println("\nAvailable Flights:");
            System.out.println("Schedule ID | Flight ID | Origin -> Destination | Departure | Arrival");
            System.out.println("--------------------------------------------------------------------------");
            for (Schedule s : matchingSchedules) {
                Flight f = s.getFlight();
                System.out.printf("%-12d | %-9d | %-6s -> %-11s | %-9s | %-9s%n",
                        s.getScheduleId(), f.getFlightId(), f.getOrigin(), f.getDestination(),
                        s.getDepartureTime().toLocalTime(), s.getArrivalTime().toLocalTime());
            }

            long scheduleId;
            while (true) {
                System.out.print("\nEnter Schedule ID to book: ");
                String input = scanner.nextLine().trim();
                try {
                    scheduleId = Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("X Invalid Schedule ID format. Please enter a valid number.");
                }
            }

            Optional<Schedule> scheduleOpt = scheduleService.getScheduleById(scheduleId);
            if (scheduleOpt.isEmpty()) {
                System.out.println("X Invalid Schedule ID.");
                return;
            }

            Schedule selectedSchedule = scheduleOpt.get();

            if (!selectedSchedule.getFlight()
                                 .getOrigin()
                                 .equalsIgnoreCase(origin)
                                 || !selectedSchedule.getFlight()
                                                     .getDestination()
                                                     .equalsIgnoreCase(destination)
                                                     || !selectedSchedule.getDepartureTime()
                                                                         .toLocalDate()
                                                                         .toString()
                                                                         .equals(departureDate))
            {
                System.out.println("X The selected schedule does not match your route and date.");
                return;
            }

            String result = bookingService.bookFlight(user.getUserId(), scheduleId);
            System.out.println(result);

        } catch (Exception e) {
            System.out.println("X An unexpected error occurred: " + e.getMessage());
        }
    }


    private void viewMyBookings(Users user) {
        System.out.println("\n===== MY BOOKINGS =====");
        List<Booking> myBookings = bookingService.getUserBookings(user);
        if (myBookings.isEmpty()) {
            System.out.println(" You have no bookings yet.");
        } else {
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            System.out.println("===================================================================================================================");
            System.out.printf("%-12s %-15s %-15s %-15s %-25s %-20s%n", 
                "Booking ID", "Flight Number", "From", "To", "Departure", "Booking Date");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            for (Booking booking : myBookings) {
                String formattedDeparture = booking.getSchedule().getDepartureTime().format(dtFormatter);
                String formattedBookingDate = booking.getBookingDate().format(dtFormatter);

                System.out.printf("%-12d %-15s %-15s %-15s %-25s %-20s%n",
                    booking.getBookingId(),
                    booking.getFlight().getFlightNumber(),
                    booking.getFlight().getOrigin(),
                    booking.getFlight().getDestination(),
                    formattedDeparture,
                    formattedBookingDate
                );
            }

            System.out.println("==================================================================================================================");
        }
    }
    
    private LocalDateTime readDateTime(Scanner scanner, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("X Invalid datetime format. Please enter in yyyy-MM-dd HH:mm format.");
            }
        }
    }
    
}