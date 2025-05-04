package com.task1.smartthings.service;


import com.task1.smartthings.model.dto.UpdateVendorDevice;
import com.task1.smartthings.model.dto.VendorDevices;
import com.task1.smartthings.repository.VendorRepository;
import ratpack.handling.Context;
import java.sql.SQLException;
import java.util.List;



public class VendorService {
    private final VendorRepository repository;
    // init
    public VendorService(VendorRepository repository) {
        this.repository = repository;
    }
    // Business Logic Method
    public List<VendorDevices> listVendorDevices(Context ctx, int vendorId) throws SQLException {
        return repository.getAllVendorDevices(ctx, vendorId);
    }

    public void addDevice(Context ctx, VendorDevices vendorDevices) {
        repository.insertVendorDevice(ctx,vendorDevices);
    }
    public void updateVendorDevice(int id, UpdateVendorDevice updateDevice) {
        repository.updateVendorDeviceById(id, updateDevice);
    }
    public void deleteDevice(int id) {
        repository.softDeleteDeviceById(id);
    }
    
}
