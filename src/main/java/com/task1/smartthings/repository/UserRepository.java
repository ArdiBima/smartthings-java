package com.task1.smartthings.repository;

import com.task1.smartthings.model.User;
import com.task1.smartthings.model.Device;
import com.task1.smartthings.model.dto.DeviceNameList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final DataSource dataSource;
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertUser(User user) {
    String sql = """
        INSERT INTO users (name, dob, address, country, email,password,created_at, updated_at)
        VALUES (?, ?, ?, ?,?,?, NOW(), NOW())
        RETURNING id
    """;

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.name);
            stmt.setDate(2, java.sql.Date.valueOf(user.dob));
            stmt.setString(3, user.address);
            stmt.setString(4, user.country);
            stmt.setString(5, user.email);
            stmt.setString(6, user.password);

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
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("failed to insert user device", e);
        }
    }
    public boolean userDeviceExists(int userId, int deviceId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM user_devices WHERE user_id = ? AND device_id = ?) AS user_device_exists;";
    
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, deviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("user_device_exists");
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }
    public void deleteUserDevice(int userId, int deviceId) {
        String sql = "DELETE FROM user_devices WHERE user_id = ? AND device_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2,deviceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to unbind device", e);
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

    public String GetUserCountryById(int userId) {
        String sql = "SELECT country FROM users WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("country");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user country", e);
        }

    }
    public List<DeviceNameList> getAvailableDevices() {
        String sql = "SELECT id, brand_name, device_name FROM devices WHERE deleted_at IS NULL";
        List<DeviceNameList> devices = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DeviceNameList device = new DeviceNameList();
                device.id = rs.getInt("id");
                device.brandName = rs.getString("brand_name");

                device.deviceName = rs.getString("device_name");

                devices.add(device);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch translated devices", e);
        }

        return devices;
    }
    public List<DeviceNameList> userBindedDevice(int userId) {
        String sql = "SELECT id, brand_name, device_name FROM user_devices ud left join devices d on d.id = ud.device_id WHERE user_id = ?";
        List<DeviceNameList> devices = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeviceNameList device = new DeviceNameList();
                    device.id = rs.getInt("id");
                    device.brandName = rs.getString("brand_name");
                    device.deviceName = rs.getString("device_name");
                    devices.add(device);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user binded devices", e);
        }
        return devices;
    }

    public Device getDeviceDetail(int deviceId){
        String sql = "SELECT id, vendor_id, brand_name, device_name, device_description, device_config_json, device_value, created_at, updated_at, deleted_at FROM devices WHERE id = ?";
        Device device = new Device();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    device.id = rs.getInt("id");
                    device.vendorId = rs.getInt("vendor_id");
                    device.brandName = rs.getString("brand_name");
                    device.deviceName = rs.getString("device_name");
                    device.deviceDescription = rs.getString("device_description");
                    device.deviceConfigJson = rs.getString("device_config_json");
                    device.deviceValue = rs.getInt("device_value");
                    device.createdAt = rs.getTimestamp("created_at");
                    device.updatedAt = rs.getTimestamp("updated_at");
                    device.deletedAt = rs.getTimestamp("deleted_at");
                } else {
                    throw new SQLException("Device not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get device detail", e);
        }
        return device;
    }
    public Integer login(String email) {
        String sql = "SELECT id FROM users WHERE email = ? ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to login", e);
        }
    }
    public String getHashedPasswordByEmail(String email){
        String sql = "SELECT password FROM users WHERE email = ? ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                } else {
                    throw new SQLException("User not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to login", e);
        }
    }
}