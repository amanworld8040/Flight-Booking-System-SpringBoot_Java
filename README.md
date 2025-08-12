# Flight Project - Spring Boot Console Backend

A fully console-based backend project built with Java and Spring Boot for managing flights. This project focuses on backend logic, running entirely from the command line, with no web UI. Ideal for learning Spring Boot fundamentals and building console utilities for flight management.

## Features

- Console-based command interface
- Manage flights, bookings, and passengers
- Built with Spring Boot and Java
- Modular structure: service, repository, and model layers
- Configurable via `application.properties`
- Extensible for new commands/features

## Technologies Used

- Java 17+
- Spring Boot
- Maven or Gradle
- JUnit (testing)

## Getting Started

### Prerequisites

- Java installed
- Maven or Gradle installed
- Git

### Building the Project

```bash
mvn clean install
# or
./gradlew build
```

### Running the Application

```bash
mvn spring-boot:run
# or, using the JAR
java -jar target/flight-console-backend.jar
```

## Project Structure

```
src/
 └── main/
     ├── java/
     │    └── com/example/flight/
     │           ├── models/
     │           ├── services/
     │           ├── repositories/
     │           └── ConsoleApplication.java
     └── resources/
         └── application.properties
```

## How to Use

- Start the application from the console.
- Enter commands to manage flights, bookings, and passengers.

### Example Commands

- `add-flight <flightNumber> <origin> <destination> <departureTime>`
- `list-flights`
- `book-ticket <flightId> <passengerName>`
- `cancel-booking <bookingId>`
- `help`

## Configuration

Edit `application.properties` for environment settings.

## Testing

```bash
mvn test
# or
./gradlew test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/my-new-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Author

- [amanworld8040](https://github.com/amanworld8040)
