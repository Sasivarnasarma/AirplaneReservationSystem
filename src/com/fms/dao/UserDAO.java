package com.fms.dao;

import com.fms.db.DBConnection;
import com.fms.exception.FMSException;
import com.fms.model.Admin;
import com.fms.model.Customer;
import com.fms.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing password security.", e);
        }
    }

    @Override
    public boolean registerUser(User user) throws FMSException {
        String sql = "INSERT INTO users (username, password, role, first_name, last_name, email, nic, passport_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashPassword(user.getPassword()));
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());

            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                stmt.setString(7, customer.getNic());
                stmt.setString(8, customer.getPassportNumber());
            } else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }

            return stmt.executeUpdate() > 0;
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            throw new FMSException("Username or Email already exists.", e);
        } catch (SQLException e) {
            throw new FMSException("Error while registering user.", e);
        }
    }

    @Override
    public User loginUser(String username, String password) throws FMSException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error during login.", e);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() throws FMSException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new FMSException("Error fetching users.", e);
        }
        return users;
    }

    @Override
    public boolean updateUserRole(int userId, String newRole) throws FMSException {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating user role.", e);
        }
    }

    @Override
    public User getUserByUsername(String username) throws FMSException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error finding user by username.", e);
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) throws FMSException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new FMSException("Error finding user by email.", e);
        }
        return null;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws FMSException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hashPassword(newPassword));
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating password.", e);
        }
    }

    @Override
    public boolean updateUser(User user) throws FMSException {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, nic=?, passport_number=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());

            if (user instanceof Customer) {
                stmt.setString(4, ((Customer) user).getNic());
                stmt.setString(5, ((Customer) user).getPassportNumber());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }

            stmt.setInt(6, user.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new FMSException("Error updating user details.", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        if ("admin".equalsIgnoreCase(role)) {
            return new Admin(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"));
        } else {
            return new Customer(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("nic"),
                    rs.getString("passport_number"));
        }
    }
}
