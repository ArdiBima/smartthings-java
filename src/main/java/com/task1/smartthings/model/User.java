package com.task1.smartthings.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class User {
    public int id;
    public String name;
    public LocalDate dob;
    public String address;
    public String country;
    public String email;
    public String password;
    public Timestamp createdAt;
    public Timestamp updatedAt;
    public Timestamp deletedAt;

}