package com.fms.model;

import java.sql.Date;
import java.sql.Time;

public class Flight {
    private int flightId;
    private String flightNumber;
    private String source;
    private String destination;
    private Date date;
    private Time time;
    private int seatsAvailable;
    private double price;
    private String status;

    public Flight(int flightId, String flightNumber, String source, String destination, Date date, Time time,
            int seatsAvailable, double price, String status) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
        this.status = status;
    }

    public Flight(String flightNumber, String source, String destination, Date date, Time time, int seatsAvailable,
            double price) {
        this.flightNumber = flightNumber;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
        this.status = "ON TIME";
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
