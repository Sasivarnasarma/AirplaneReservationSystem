package com.fms.dao;

public class DAOFactory {
    public static IUserDAO getUserDAO() {
        return new UserDAO();
    }

    public static IFlightDAO getFlightDAO() {
        return new FlightDAO();
    }

    public static IBookingDAO getBookingDAO() {
        return new BookingDAO();
    }
}
