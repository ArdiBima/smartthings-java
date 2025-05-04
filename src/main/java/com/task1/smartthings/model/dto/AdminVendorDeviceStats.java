package com.task1.smartthings.model.dto;

public class AdminVendorDeviceStats {
    private int deviceId;
    private int vendorId;
    private String brandName;
    private String deviceName;
    private int userCount;
    
    // Getters & setters
    public int getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
    
    public int getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }
    
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public int getUserCount() {
        return userCount;
    }
    
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
