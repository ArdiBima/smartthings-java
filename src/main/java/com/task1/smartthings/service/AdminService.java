package com.task1.smartthings.service;

import com.task1.smartthings.model.Device;
import com.task1.smartthings.model.User;
import com.task1.smartthings.model.dto.AdminUserDeviceStats;
import com.task1.smartthings.model.dto.AdminVendorDeviceStats;
import com.task1.smartthings.repository.AdminRepository;
import ratpack.handling.Context;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final AdminRepository repository;
    // init
    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }
    // Business Logic Method
    public List<Device> listDevices() throws SQLException {
        return repository.getAllDevices();
    }

    public User UserDetail(Context ctx, int userId) throws SQLException {
        return repository.getUserDetail(ctx, userId);
    }
    public List<AdminVendorDeviceStats> getVendorDeviceStats() {
        return repository.getVendorDeviceStats();
    }

    public List<AdminUserDeviceStats> getUserDeviceStats() {
        return repository.getUserDeviceStats();
    }
}
