package com.task1.smartthings.repository;

import com.task1.smartthings.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserRepository {
    private final DataSource dataSource;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertUser(User user) {
    String sql = """
        INSERT INTO users (name, dob, address, country, created_at, updated_at)
        VALUES (?, ?, ?, ?, NOW(), NOW())
        RETURNING id
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.name);
            stmt.setDate(2, java.sql.Date.valueOf(user.dob));
            stmt.setString(3, user.address);
            stmt.setString(4, user.country);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("User insert failed, no ID returned.");
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to insert user", e);
    }
}
    public boolean userExists(String name, LocalDate dob) {
        String sql = "SELECT 1 FROM users WHERE name = ? AND dob = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDate(2, java.sql.Date.valueOf(dob));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If there's a result, a user exists
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }

    public void insertUserDevice(int userId, int deviceId) {
        String sql = "INSERT INTO user_devices (user_id, device_id) VALUES (?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2,deviceId);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }
    public boolean userDeviceExists(int userId, int deviceId) {
        String sql = "SELECT 1 FROM user_devices WHERE user_id = ? AND device_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, deviceId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If there's a result, a user exists
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }
    public void deleteUserDevice(int userId, int deviceId) {
        String sql = "DELETE FROM user_devices WHERE user_id = ? AND device_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2,deviceId);
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }
    public void updateDeviceValueById(int deviceId, int newValue) {
        String sql = "UPDATE devices SET device_value = ?, updated_at = now() WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, newValue);
            stmt.setInt(2, deviceId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update device_value for device ID: " + deviceId, e);
        }
    }
}