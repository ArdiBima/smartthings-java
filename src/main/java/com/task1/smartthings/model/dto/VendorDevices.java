package com.task1.smartthings.model.dto;
import java.sql.Timestamp;

public class VendorDevices {
    public int id;
    public int vendorId;
    public int value;
    public String brandName;
    public String deviceName;
    public String deviceDescription;
    public DeviceConfigJson deviceConfigJson;
    public int deviceValue;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public Timestamp deletedAt;

    // Getters
    public int getVendorId() {
        return vendorId;
    }

    public int getValue() {
        return value;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public DeviceConfigJson getDeviceConfigJson() {
        return deviceConfigJson;
    }

    public int getDeviceValue() {
        return deviceValue;
    }

    // Setters
    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public void setDeviceConfigJson(DeviceConfigJson deviceConfigJson) {
        this.deviceConfigJson = deviceConfigJson;
    }

    public void setDeviceValue(int deviceValue) {
        this.deviceValue = deviceValue;
    }
}

