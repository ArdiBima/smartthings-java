package com.task1.smartthings.model.dto;

import java.sql.Timestamp;

public class UserDetail {
    public int id;
    public String name;
    public String dob; 
    public String address;
    public String country;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public Timestamp deletedAt;
}