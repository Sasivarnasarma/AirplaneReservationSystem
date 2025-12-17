package com.fms.dao;

import com.fms.exception.FMSException;
import com.fms.model.Flight;
import java.sql.Date;
import java.util.List;

public interface IFlightDAO {
    boolean addFlight(Flight flight) throws FMSException;

    List<Flight> getAllFlights() throws FMSException;

    List<Flight> getAvailableFlights() throws FMSException;

    List<Flight> searchFlights(String source, String destination, Date date) throws FMSException;

    boolean updateSeats(int flightId, int seatsToBook) throws FMSException;

    boolean updateFlightStatus(int flightId, String status) throws FMSException;

    List<String> getDistinctSources() throws FMSException;

    List<String> getDistinctDestinations() throws FMSException;
}
