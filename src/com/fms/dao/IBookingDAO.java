package com.fms.dao;

import com.fms.exception.FMSException;
import com.fms.model.Booking;
import java.util.List;

public interface IBookingDAO {
    boolean createBooking(Booking booking) throws FMSException;

    boolean updateBookingStatus(int bookingId, String status) throws FMSException;

    List<String> getBookedSeats(int flightId) throws FMSException;

    List<Booking> getBookingsByUserId(int userId) throws FMSException;

    List<Booking> getBookingsByFlightId(int flightId) throws FMSException;

    List<Booking> getAllBookings() throws FMSException;
}
