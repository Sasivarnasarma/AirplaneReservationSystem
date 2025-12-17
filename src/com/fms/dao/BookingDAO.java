package com.fms.dao;

import com.fms.db.DBConnection;
import com.fms.exception.FMSException;
import com.fms.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO implements IBookingDAO {

    public boolean createBooking(Booking booking) throws FMSException {
        String sqlBooking = "INSERT INTO bookings (user_id, flight_id, status) VALUES (?, ?, ?)";
        String sqlSeats = "INSERT INTO booked_seats (booking_id, seat_number) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmtBooking = null;
        PreparedStatement stmtSeats = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            stmtBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
            stmtBooking.setInt(1, booking.getUserId());
            stmtBooking.setInt(2, booking.getFlightId());
            stmtBooking.setString(3, "PENDING");

            int affected = stmtBooking.executeUpdate();
            if (affected == 0) {
                conn.rollback();
                throw new FMSException("Creating booking failed, no rows affected.");
            }

            int bookingId = -1;
            rs = stmtBooking.getGeneratedKeys();
            if (rs.next()) {
                bookingId = rs.getInt(1);
            }

            if (bookingId != -1 && booking.getSeatNumbers() != null && !booking.getSeatNumbers().isEmpty()) {
                stmtSeats = conn.prepareStatement(sqlSeats);
                String[] seats = booking.getSeatNumbers().split(",");
                for (String seat : seats) {
                    stmtSeats.setInt(1, bookingId);
                    stmtSeats.setString(2, seat.trim());
                    stmtSeats.addBatch();
                }
                stmtSeats.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new FMSException("Transaction rollback failed: " + ex.getMessage(), ex);
                }
            }
            throw new FMSException("Error creating booking transaction: " + e.getMessage(), e);
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
                if (rs != null)
                    rs.close();
                if (stmtBooking != null)
                    stmtBooking.close();
                if (stmtSeats != null)
                    stmtSeats.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                throw new FMSException("Error closing resources.", e);
            }
        }
    }

    public boolean updateBookingStatus(int bookingId, String status) throws FMSException {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, bookingId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating booking status.", e);
        }
    }

    public List<String> getBookedSeats(int flightId) throws FMSException {
        List<String> seats = new ArrayList<>();
        String sql = "SELECT bs.seat_number FROM booked_seats bs " +
                "JOIN bookings b ON bs.booking_id = b.booking_id " +
                "WHERE b.flight_id = ? AND b.status != 'CANCELLED'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(rs.getString("seat_number"));
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error fetching booked seats.", e);
        }
        return seats;
    }

    public List<Booking> getBookingsByUserId(int userId) throws FMSException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, f.flight_number, f.source, f.destination, f.price, b.booking_date, b.status, "
                +
                "GROUP_CONCAT(bs.seat_number SEPARATOR ',') as seat_numbers " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "LEFT JOIN booked_seats bs ON b.booking_id = bs.booking_id " +
                "WHERE b.user_id = ? " +
                "GROUP BY b.booking_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String seatNums = rs.getString("seat_numbers");
                    int seatCount = seatNums != null && !seatNums.isEmpty() ? seatNums.split(",").length : 0;
                    double price = rs.getDouble("price");
                    bookings.add(new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("flight_number"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getTimestamp("booking_date"),
                            rs.getString("status"),
                            seatNums,
                            price * seatCount));
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error fetching bookings for user.", e);
        }
        return bookings;
    }

    public List<Booking> getBookingsByFlightId(int flightId) throws FMSException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, f.flight_number, f.source, f.destination, f.price, b.booking_date, b.status, u.username, "
                +
                "GROUP_CONCAT(bs.seat_number SEPARATOR ',') as seat_numbers " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "LEFT JOIN booked_seats bs ON b.booking_id = bs.booking_id " +
                "WHERE b.flight_id = ? " +
                "GROUP BY b.booking_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String seatNums = rs.getString("seat_numbers");
                    int seatCount = seatNums != null && !seatNums.isEmpty() ? seatNums.split(",").length : 0;
                    double price = rs.getDouble("price");
                    bookings.add(new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("flight_number"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getTimestamp("booking_date"),
                            rs.getString("status"),
                            seatNums,
                            rs.getString("username"),
                            price * seatCount));
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error fetching bookings for flight.", e);
        }
        return bookings;
    }

    public List<Booking> getAllBookings() throws FMSException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, f.flight_number, f.source, f.destination, f.price, b.booking_date, b.status, u.username, "
                +
                "GROUP_CONCAT(bs.seat_number SEPARATOR ',') as seat_numbers " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "LEFT JOIN booked_seats bs ON b.booking_id = bs.booking_id " +
                "GROUP BY b.booking_id";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String seatNums = rs.getString("seat_numbers");
                int seatCount = seatNums != null && !seatNums.isEmpty() ? seatNums.split(",").length : 0;
                double price = rs.getDouble("price");
                bookings.add(new Booking(
                        rs.getInt("booking_id"),
                        rs.getString("flight_number"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getTimestamp("booking_date"),
                        rs.getString("status"),
                        seatNums,
                        rs.getString("username"),
                        price * seatCount));
            }
        } catch (SQLException e) {
            throw new FMSException("Error fetching all bookings.", e);
        }
        return bookings;
    }
}
