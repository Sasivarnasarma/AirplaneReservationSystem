# Airplane Reservation System (IM Airlines)

## Overview
The Airplane Reservation System is a comprehensive Java-based desktop application designed to manage flight bookings, user registrations, and administrative tasks. Built with Swing for the GUI and MySQL for the database, it follows the MVC (Model-View-Controller) pattern and utilizes robust Object-Oriented Programming (OOP) principles.

## Tech Stack
*   **Language**: Java (JDK 8+)
*   **GUI Framework**: Java Swing (javax.swing, java.awt)
*   **Database**: MySQL
*   **Connectivity**: JDBC (Java Database Connectivity)
*   **IDE**: VS Code / Eclipse / IntelliJ IDEA

## Project Structure
```
AirplaneReservationSystemAG
├── lib/                  # External libraries (MySQL Connector)
├── src/com/fms
│   ├── dao/              # Data Access Objects (DB operations)
│   ├── db/               # Database Connection configuration
│   ├── model/            # Data Models (User, Flight, Booking)
│   ├── ui/               # User Interface (Forms, Dashboards)
│   ├── exception/        # Custom Exceptions
│   └── Main.java         # Application Entry Point
├── schema.sql            # Database creation script
└── README.md             # Project Documentation
```

## Getting Started

### 1. Prerequisites
*   Java Development Kit (JDK) 8 or higher.
*   MySQL Server installed and running.

### 2. Database Setup
1.  Open your MySQL Client (Workbench or Command Line).
2.  Run the `schema.sql` script located in the project root. This will:
    *   Create the database `flight_mgmt_db`.
    *   Create necessary tables (`users`, `flights`, `bookings`, etc.).
    *   Insert initial seed data (admin users, sample flights).

### 3. Configuration
1.  Navigate to `src/com/fms/db/DBConnection.java`.
2.  Update the `USER` and `PASSWORD` constants to match your local MySQL credentials:
    ```java
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_PASSWORD";
    ```

### 4. Running the Application
*   Compile the project ensuring the MySQL Connector JAR (in `lib/`) is in your classpath.
*   Run `com.fms.Main`.
*   **Login**:
    *   **Admin**: username: `admin`, password: `123` (default from schema.sql)
    *   **Customer**: Register a new account via the UI.

## Features

### 1. User Management
*   **Registration**: Users can create accounts with validation for personal details (Email, NIC, Passport).
*   **Authentication**: Secure login system with role-based access (Customer vs. Admin).
*   **Profile Management**: users can update their personal information and change passwords.
*   **OTP Verification**: A mock email OTP system for verifying user identity during registration.

### 2. Flight Management
*   **Search**: Customers can search for flights based on source, destination, and date.
*   **Real-time Availability**: View available seats and flight status (Scheduled/Cancelled).

### 3. Booking System
*   **Seat Selection**: Interactive booking process allowing users to select the number of seats (1-6).
*   **Transaction Safety**: Ensures bookings are atomic; if any part of the booking fails, the entire transaction is rolled back.
*   **Payment Simulation**: A mock payment gateway to process ticket costs.
*   **Ticket Generation**: Automatically generates a downloadable ticket (Text/PDF format) upon successful booking.

### 4. Admin Dashboard
*   Manage flights (Add, Update, Remove).
*   View all user bookings.

## OOP Concepts Used

We have leveraged key OOP pillars to ensure code maintainability and scalability:

*   **Encapsulation**:
    *   All data models (e.g., `User`, `Flight`, `Booking`) use private fields with public Getters and Setters to protect data integrity.
    *   Database connection logic is encapsulated within the `DBConnection` class.

*   **Inheritance**:
    *   **Code Reuse**: The `Customer` and `Admin` classes inherit from the abstract `User` base class to share common attributes like username, password, and contact info.
    *   **UI Components**: Custom UI classes (e.g., `RegisterFrame`, `CustomerDashboard`) extend `JFrame` or `JPanel`.

*   **Polymorphism & Abstraction**:
    *   **Interfaces**: We use DAO (Data Access Object) interfaces (`IUserDAO`, `IFlightDAO`, `IBookingDAO`) to define contracts for database operations. This abstracts the implementation details from the business logic.
    *   **Abstract Classes**: The `User` class is abstract, preventing direct instantiation of a generic user while allowing specific roles to be instantiated.

*   **Design Patterns**:
    *   **DAO Pattern**: Separates the data persistence layer from the application logic.
    *   **Factory Pattern**: `DAOFactory` is used to instantiate and provide access to DAO implementations, promoting loose coupling.
    *   **Singleton (Implicit)**: Database connection is managed centrally.

## Error Handling

The application implements a robust error handling strategy:

1.  **Custom Exceptions**: A dedicated `FMSException` class wraps lower-level exceptions (like `SQLException`), providing meaningful error messages to the upper layers.
2.  **Try-Catch Blocks**: Utilized extensively in DAO classes to catch database errors.
3.  **Transaction Management**: In `BookingDAO`, `commit()` and `rollback()` are used within try-catch blocks to ensure data consistency during complex operations (e.g., creating a booking *and* reserving seats simultaneously).
4.  **User Feedback**: Errors are not just logged; they are displayed to the user via friendly `JOptionPane` or custom `ModernDialog` messages (e.g., "Registration failed", "Invalid OTP").

## Validations

Key input and logic validations ensure data quality:

*   **Input Regex**:
    *   **Email**: Validates standard email formats.
    *   **NIC**: Checks for 9-12 alphanumeric characters.
    *   **Passport**: Checks for 6-9 alphanumeric characters.
    *   **Password**: Enforces complexity (at least 6 chars, letters, numbers, and symbols).
*   **Logic Validation**:
    *   Prevents booking more seats than available.
    *   Checks if a username or email is already taken during registration.
    *   Ensures booking dates are valid.

## File Handling

The application utilizes Java's I/O (Input/Output) capabilities for generating and managing ticket files:

*   **Ticket Generation**:
    *   Uses `java.io.FileWriter` to create a text-based ticket file (`Ticket_<BookingID>.txt`) locally on the user's machine.
    *   The file contains detailed booking information including passenger name, flight details, and seat numbers.
*   **Automatic Opening**:
    *   Leverages `java.awt.Desktop` to automatically open the generated ticket file with the system's default text editor immediately after download.
*   **Exception Handling**:
    *   `IOException` is caught and handled to prevent application crashes during file write operations.

## Special Features

*   **Custom UI Theme**: A `StyleTheme` class ensures a consistent, modern look and feel (colors, fonts, button styles) across the application.
*   **Custom Painting**: Many Swing components (Buttons, TextFields) have overridden `paintComponent` methods to achieve rounded corners and hover effects, moving beyond standard 'look and feel'.
*   **Database Transactions**: Connection auto-commit is disabled for critical sections to prevent partial data writes.

## Future Improvements
*   **Email Integration**: Real email sending for OTPs and Tickets using JavaMail API.
*   **Payment Gateway**: Integration with Stripe or PayPal for real transactions.
*   **Flight API**: Fetching real-time flight data from external aviation APIs.
*   **Admin Reports**: Generating PDF reports for sales and flight utilization.