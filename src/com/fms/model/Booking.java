package com.fms.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int userId;
    private int flightId;
    private Timestamp bookingDate;
    private String status;
    private String seatNumbers;
    private String flightNumber;
    private String source;
    private String destination;
    private String username;
    private double totalPrice;

    public Booking(int bookingId, int userId, int flightId, Timestamp bookingDate, String status, String seatNumbers) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatNumbers = seatNumbers;
    }

    public Booking(int userId, int flightId, String seatNumbers) {
        this.userId = userId;
        this.flightId = flightId;
        this.status = "CONFIRMED";
        this.seatNumbers = seatNumbers;
    }

    public Booking(int bookingId, String flightNumber, String source, String destination, Timestamp bookingDate,
            String status, String seatNumbers, double totalPrice) {
        this(bookingId, flightNumber, source, destination, bookingDate, status, seatNumbers, null, totalPrice);
    }

    public Booking(int bookingId, String flightNumber, String source, String destination, Timestamp bookingDate,
            String status, String seatNumbers, String username, double totalPrice) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatNumbers = seatNumbers;
        this.username = username;
        this.totalPrice = totalPrice;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFlightId() {
        return flightId;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getUsername() {
        return username;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
