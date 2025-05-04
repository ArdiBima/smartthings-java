package com.task1.smartthings.model;
import java.sql.Timestamp;
public class Device {
    public int id;
    public int vendorId;
    public String brandName;
    public String deviceName;
    public String deviceDescription;
    public String deviceConfigJson;
    public int deviceValue;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public Timestamp deletedAt;

    
}
