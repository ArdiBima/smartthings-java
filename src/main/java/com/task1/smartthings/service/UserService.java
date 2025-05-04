package com.task1.smartthings.service;




import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.task1.smartthings.model.User;
import com.task1.smartthings.model.dto.UserRegisterDTO;
import com.task1.smartthings.repository.UserRepository;


public class UserService {
    private final UserRepository repository;
    // init
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public int registerUser(UserRegisterDTO dto) {
        User user = new User();
        user.name = dto.getName();

        // Parse the DOB string into a LocalDate
        try {
            // Assumes dto.dob is in ISO format "yyyy-MM-dd"
            user.dob = LocalDate.parse(dto.getDob(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dob, expected yyyy-MM-dd", e);
        }

        user.address = dto.getAddress();
        user.country = dto.getCountry();

        // Check if a user with the same name and DOB already exists
        
            boolean userExist=repository.userExists(user.name, user.dob);
            if (userExist){
                throw new RuntimeException("User is already exist.");
            }
            // Ignore, user doesn't exist
            
        

        // Insert the user if no duplicate found
        int userId = repository.insertUser(user);

        return userId;
    }
    public void bindDeviceUserService(int userId, int deviceId) {
        boolean userDeviceExist=repository.userDeviceExists(userId, deviceId);
        if (userDeviceExist){
            throw new RuntimeException("User is already binded to device.");
        }
        repository.insertUserDevice(userId, deviceId);
    }
    public void unbindDeviceUserService(int userId, int deviceId) {
        boolean userDeviceExist=repository.userDeviceExists(userId, deviceId);
        if (!userDeviceExist){
            throw new RuntimeException("User is not binded to device.");
        }
        repository.deleteUserDevice(userId, deviceId);
    }
    public void updateDeviceValueById(int userId,int deviceId, int newValue) {
        boolean userDeviceExist=repository.userDeviceExists(userId, deviceId);
        if (!userDeviceExist){
            throw new RuntimeException("User is not binded to device.");
        }
        repository.updateDeviceValueById(deviceId, newValue);
    }
}