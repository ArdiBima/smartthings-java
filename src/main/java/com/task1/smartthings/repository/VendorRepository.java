package com.task1.smartthings.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.model.dto.DeviceConfigJson;
import com.task1.smartthings.model.dto.VendorDevices;
import com.task1.smartthings.model.dto.UpdateVendorDevice;

import com.task1.smartthings.service.VendorService;
import ratpack.handling.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorRepository {
    private final DataSource dataSource;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(VendorService.class);
    public VendorRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        
    }

    public List<VendorDevices> getAllVendorDevices(Context ctx,int id) throws SQLException {
        List<VendorDevices> devices = new ArrayList<>();
        String query = "SELECT id, vendor_id, brand_name, device_name, device_description, device_config_json, device_value, created_at, updated_at, deleted_at FROM devices WHERE vendor_id = ? AND deleted_at IS NULL";
        
        try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VendorDevices d = new VendorDevices();
                    d.id = rs.getInt("id");
                    d.vendorId = rs.getInt("vendor_id");
                    d.brandName = rs.getString("brand_name");
                    d.deviceName = rs.getString("device_name");
                    d.deviceDescription = rs.getString("device_description");
                    d.deviceValue = rs.getInt("device_value");
                    d.createdAt = rs.getTimestamp("created_at");
                    d.updatedAt = rs.getTimestamp("updated_at");
                    d.deletedAt = rs.getTimestamp("deleted_at");

                    try {
                        String configJson = rs.getString("device_config_json");
                        if (configJson != null) {
                            d.deviceConfigJson = mapper.readValue(configJson, DeviceConfigJson.class);
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to parse deviceConfigJson", e);
                    }

                    devices.add(d);
                }
            }
        }
        return devices;
    }
    
    public void insertVendorDevice(Context ctx, VendorDevices vendorDevices) {
        logger.info("Inserting device for vendorId={}", vendorDevices.getVendorId());
        String sql = """
            INSERT INTO devices (
                vendor_id, brand_name, device_name,
                device_description, device_config_json, device_value,
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?::jsonb, ?, NOW(), NOW())
        """;
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendorDevices.getVendorId());
            stmt.setString(2, vendorDevices.getBrandName());
            stmt.setString(3, vendorDevices.getDeviceName());
            stmt.setString(4, vendorDevices.getDeviceDescription());
    
            try {
                String json = mapper.writeValueAsString(vendorDevices.getDeviceConfigJson());
                stmt.setString(5, json);  
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize deviceConfigJson", e);
            }
    
            stmt.setInt(6, vendorDevices.getDeviceValue());
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert device", e);
        }
    }
    
    public void updateVendorDeviceById(int id,int vendorId, UpdateVendorDevice update) {
        StringBuilder sql = new StringBuilder("UPDATE devices SET ");
        List<Object> params = new ArrayList<>();
    
        if (update.vendorId != null) {
            sql.append("vendor_id = ?, ");
            params.add(update.vendorId);
        }
        if (update.brandName != null) {
            sql.append("brand_name = ?, ");
            params.add(update.brandName);
        }
        if (update.deviceName != null) {
            sql.append("device_name = ?, ");
            params.add(update.deviceName);
        }
        if (update.deviceDescription != null) {
            sql.append("device_description = ?, ");
            params.add(update.deviceDescription);
        }
        if (update.deviceConfigJson != null) {
            if (update.deviceValue != null) {
                update.deviceConfigJson.defaultValue = update.deviceValue; 
            }
    
            ObjectMapper mapper = new ObjectMapper();
            try {
                String configJson = mapper.writeValueAsString(update.deviceConfigJson);
                sql.append("device_config_json = ?::jsonb, ");
                params.add(configJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON serialization failed", e);
            }
        }
        if (update.deviceValue != null) {
            sql.append("device_value = ?, ");
            params.add(update.deviceValue);
        }
    
        sql.append("updated_at = NOW() WHERE id = ? AND vendor_id = ? AND deleted_at IS NULL");
        params.add(id);
        params.add(vendorId);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
    
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update device", e);
        }
    }
    public void softDeleteDeviceById(int id,int vendorId) {
        String sql = "UPDATE devices SET deleted_at = NOW() WHERE id = ? AND vendor_id = ? AND deleted_at IS NULL";
    
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, vendorId);
            int affectedRows = stmt.executeUpdate();
    
            if (affectedRows == 0) {
                throw new RuntimeException("No device found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to soft delete device with id: " + id, e);
        }
    }
    
}
