package com.task1.smartthings.service;

import com.task1.smartthings.model.Device;
import com.task1.smartthings.model.User;
import com.task1.smartthings.model.dto.AdminUserDeviceStats;
import com.task1.smartthings.model.dto.AdminVendorDeviceStats;
import com.task1.smartthings.model.dto.UserDetail;
import com.task1.smartthings.repository.AdminRepository;
import ratpack.handling.Context;

import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }
    
    public List<Device> listDevices() throws SQLException {
        return repository.getAllDevices();
    }

    public UserDetail UserDetail(Context ctx, int userId) throws SQLException {
        User userDetail = repository.getUserDetail(ctx, userId);
        UserDetail response = new UserDetail();

        response.id = userDetail.id;
        response.name = userDetail.name;
        response.dob = userDetail.dob != null ? userDetail.dob.toString() : null;
        response.address = userDetail.address;
        response.country = userDetail.country;
        response.createdAt = userDetail.createdAt;
        response.updatedAt = userDetail.updatedAt;
        response.deletedAt = userDetail.deletedAt;

        return response;
    }
    public List<AdminVendorDeviceStats> getVendorDeviceStats() {
        return repository.getVendorDeviceStats();
    }

    public List<AdminUserDeviceStats> getUserDeviceStats() {
        return repository.getUserDeviceStats();
    }
}
