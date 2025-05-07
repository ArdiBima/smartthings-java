package com.task1.smartthings.service;


import com.task1.smartthings.model.dto.UpdateVendorDevice;
import com.task1.smartthings.model.dto.VendorDevices;
import com.task1.smartthings.repository.VendorRepository;
import ratpack.handling.Context;
import java.sql.SQLException;
import java.util.List;



public class VendorService {
    private final VendorRepository repository;

    public VendorService(VendorRepository repository) {
        this.repository = repository;
    }

    public List<VendorDevices> listVendorDevices(Context ctx, int vendorId) throws SQLException {
        return repository.getAllVendorDevices(ctx, vendorId);
    }

    public void addDevice(Context ctx, VendorDevices vendorDevices) {
        repository.insertVendorDevice(ctx,vendorDevices);
        
    }
    public void updateVendorDevice(int id, int vendorId, UpdateVendorDevice updateDevice) {
        repository.updateVendorDeviceById(id,vendorId ,updateDevice);
    }
    public void deleteDevice(int id,int vendorId) {
        repository.softDeleteDeviceById(id,vendorId);
    }
    
}
