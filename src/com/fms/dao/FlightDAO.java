package com.fms.dao;

import com.fms.db.DBConnection;
import com.fms.exception.FMSException;
import com.fms.model.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO implements IFlightDAO {

    public boolean addFlight(Flight flight) throws FMSException {
        String sql = "INSERT INTO flights (flight_number, source, destination, flight_date, flight_time, seats_available, price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getSource());
            stmt.setString(3, flight.getDestination());
            stmt.setDate(4, flight.getDate());
            stmt.setTime(5, flight.getTime());
            stmt.setInt(6, flight.getSeatsAvailable());
            stmt.setDouble(7, flight.getPrice());
            stmt.setString(8, "ON TIME");

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error adding flight: " + e.getMessage(), e);
        }
    }

    public List<Flight> getAllFlights() throws FMSException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new FMSException("Error retrieving all flights.", e);
        }
        return flights;
    }

    public List<Flight> getAvailableFlights() throws FMSException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE TIMESTAMP(flight_date, flight_time) > NOW()";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new FMSException("Error retrieving available flights.", e);
        }
        return flights;
    }

    public List<Flight> searchFlights(String source, String destination, Date date) throws FMSException {
        List<Flight> flights = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM flights WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (source != null && !source.trim().isEmpty()) {
            sql.append(" AND source LIKE ?");
            params.add("%" + source.trim() + "%");
        }
        if (destination != null && !destination.trim().isEmpty()) {
            sql.append(" AND destination LIKE ?");
            params.add("%" + destination.trim() + "%");
        }
        if (date != null) {
            sql.append(" AND flight_date = ?");
            params.add(date);
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    flights.add(mapResultSetToFlight(rs));
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error searching flights.", e);
        }
        return flights;
    }

    public boolean updateSeats(int flightId, int seatsToBook) throws FMSException {
        String sql = "UPDATE flights SET seats_available = seats_available - ? WHERE flight_id = ? AND seats_available >= ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seatsToBook);
            stmt.setInt(2, flightId);
            stmt.setInt(3, seatsToBook);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating seats.", e);
        }
    }

    public boolean updateFlightStatus(int flightId, String status) throws FMSException {
        String sql = "UPDATE flights SET status = ? WHERE flight_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, flightId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating flight status.", e);
        }
    }

    public List<String> getDistinctSources() throws FMSException {
        List<String> sources = new ArrayList<>();
        String sql = "SELECT DISTINCT source FROM flights ORDER BY source";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sources.add(rs.getString("source"));
            }
        } catch (SQLException e) {
            throw new FMSException("Error retrieving sources.", e);
        }
        return sources;
    }

    public List<String> getDistinctDestinations() throws FMSException {
        List<String> destinations = new ArrayList<>();
        String sql = "SELECT DISTINCT destination FROM flights ORDER BY destination";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                destinations.add(rs.getString("destination"));
            }
        } catch (SQLException e) {
            throw new FMSException("Error retrieving destinations.", e);
        }
        return destinations;
    }

    private Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        String status = "ON TIME";
        try {
            status = rs.getString("status");
            if (status == null)
                status = "ON TIME";
        } catch (SQLException e) {
        }

        return new Flight(
                rs.getInt("flight_id"),
                rs.getString("flight_number"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getDate("flight_date"),
                rs.getTime("flight_time"),
                rs.getInt("seats_available"),
                rs.getDouble("price"),
                status);
    }
}
