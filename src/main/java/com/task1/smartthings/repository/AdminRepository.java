package com.task1.smartthings.repository;

import com.task1.smartthings.model.Device;
import com.task1.smartthings.model.User;
import com.task1.smartthings.model.dto.AdminUserDeviceStats;
import com.task1.smartthings.model.dto.AdminVendorDeviceStats;

import ratpack.handling.Context;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    private final DataSource dataSource;

    public AdminRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Device> getAllDevices() throws SQLException {
        List<Device> devices = new ArrayList<>();
        String query = "SELECT id, vendor_id, brand_name, device_name, device_description, device_config_json, device_value, created_at, updated_at, deleted_at FROM devices";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Device d = new Device();
                d.id = rs.getInt("id");
                d.vendorId = rs.getInt("vendor_id");
                d.brandName = rs.getString("brand_name");
                d.deviceName = rs.getString("device_name");
                d.deviceDescription = rs.getString("device_description");
                d.deviceConfigJson = rs.getString("device_config_json");
                d.deviceValue = rs.getInt("device_value");
                d.createdAt = rs.getTimestamp("created_at");
                d.updatedAt = rs.getTimestamp("updated_at");
                d.deletedAt = rs.getTimestamp("deleted_at");
                devices.add(d);
            }
        }
        return devices;
    }
    
    public User getUserDetail(Context ctx, int userId) throws SQLException {
        String sql = "SELECT id, name, dob, address, country, created_at, updated_at, deleted_at FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.id = rs.getInt("id");
                    user.name = rs.getString("name");
                    user.dob = rs.getDate("dob").toLocalDate();
                    user.address = rs.getString("address");
                    user.country = rs.getString("country");
                    user.createdAt = rs.getTimestamp("created_at");
                    user.updatedAt = rs.getTimestamp("updated_at");
                    user.deletedAt = rs.getTimestamp("deleted_at");
                    return user;
                } else {
                    throw new SQLException("User not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user detail", e);
        }
        
    }
    public List<AdminVendorDeviceStats> getVendorDeviceStats() {
        List<AdminVendorDeviceStats> result = new ArrayList<>();
        String sql = """
            SELECT 
                d.id AS device_id,
                v.id AS vendor_id,
                v.brand_name,
                d.device_name,
                COUNT(ud.user_id) AS user_count
            FROM devices d
            JOIN vendors v ON d.vendor_id = v.id
            LEFT JOIN user_devices ud ON d.id = ud.device_id
            GROUP BY d.id, v.id, v.brand_name, d.device_name
            ORDER BY user_count DESC
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AdminVendorDeviceStats stats = new AdminVendorDeviceStats();
                stats.setDeviceId(rs.getInt("device_id"));
                stats.setVendorId(rs.getInt("vendor_id"));
                stats.setBrandName(rs.getString("brand_name"));
                stats.setDeviceName(rs.getString("device_name"));
                stats.setUserCount(rs.getInt("user_count"));
                result.add(stats);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch vendor device stats", e);
        }

        return result;
    }
    public List<AdminUserDeviceStats> getUserDeviceStats() {
        List<AdminUserDeviceStats> result = new ArrayList<>();
        String sql = """
            SELECT 
                u.id AS user_id,
                u.name,
                COUNT(ud.device_id) AS device_count
            FROM users u
            LEFT JOIN user_devices ud ON u.id = ud.user_id
            GROUP BY u.id, u.name
            ORDER BY device_count DESC
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AdminUserDeviceStats stats = new AdminUserDeviceStats();
                stats.setUserId(rs.getInt("user_id"));
                stats.setName(rs.getString("name"));
                stats.setDeviceCount(rs.getInt("device_count"));
                result.add(stats);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user device stats", e);
        }

        return result;
    }
}
