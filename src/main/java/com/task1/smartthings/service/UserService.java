package com.task1.smartthings.service;




import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.task1.smartthings.service.pkg.ThirdPartyTranslator;
import com.task1.smartthings.util.JwtUtilRSA;
import com.task1.smartthings.util.PasswordUtil;
import com.task1.smartthings.model.User;
import com.task1.smartthings.model.Device;

import com.task1.smartthings.model.dto.DeviceNameList;
import com.task1.smartthings.model.dto.UserRegisterDTO;
import com.task1.smartthings.model.dto.DeviceConfigJson;
import com.task1.smartthings.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService {
    private final UserRepository repository;
    private final ThirdPartyTranslator translator;
    private final ObjectMapper mapper;
    private final JwtUtilRSA jwtUtilRSA;
    // init
    public UserService(UserRepository repository, ThirdPartyTranslator translator, ObjectMapper mapper,JwtUtilRSA jwtUtilRSA) {
        this.repository = repository;
        this.translator = translator;
        this.mapper = mapper;
        this.jwtUtilRSA = jwtUtilRSA;
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
        user.email = dto.getEmail();
        String hashedPassword;
        try {
            hashedPassword = PasswordUtil.hashPassword(dto.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
        user.password = hashedPassword;
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
        Device deviceDetail = repository.getDeviceDetail(deviceId);
        DeviceConfigJson configJson;
        try {
            configJson= mapper.readValue(deviceDetail.deviceConfigJson, DeviceConfigJson.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse device configuration JSON", e);
        }
        if (newValue < configJson.minValue || newValue > configJson.maxValue) {
            throw new RuntimeException("Value is out of range");
        }
        repository.updateDeviceValueById(deviceId, newValue);
    }
    public List<Map<String, Object>> getTranslatedAvailableDevices(int userId) throws IOException {
        String userCountry = repository.GetUserCountryById(userId);
        List<DeviceNameList> devices = repository.getAvailableDevices();
        List<Map<String, Object>> translatedDevices = new ArrayList<>();
    
        for (DeviceNameList device : devices) {
            String translatedName = translator.translateDeviceName(device.deviceName, userCountry);
    
            Map<String, Object> deviceMap = new HashMap<>();
            deviceMap.put("id", device.id);
            deviceMap.put("brandName", device.brandName);
            deviceMap.put("deviceName", device.deviceName); 
            deviceMap.put("translatedName", translatedName);
    
            translatedDevices.add(deviceMap);
        }
    
        return translatedDevices;
    }
    public List<Map<String, Object>> getUserBindedDevices(int userId) throws IOException {
        String userCountry = repository.GetUserCountryById(userId);
        List<DeviceNameList> devices = repository.userBindedDevice(userId);
        List<Map<String, Object>> translatedDevices = new ArrayList<>();
    
        for (DeviceNameList device : devices) {
            String translatedName = translator.translateDeviceName(device.deviceName, userCountry);
    
            Map<String, Object> deviceMap = new HashMap<>();
            deviceMap.put("id", device.id);
            deviceMap.put("brandName", device.brandName);
            deviceMap.put("deviceName", device.deviceName); 
            deviceMap.put("translatedName", translatedName);
    
            translatedDevices.add(deviceMap);
        }
    
        return translatedDevices;
    }

    public String login(String email, String password) {
    String storedHashedPassword = repository.getHashedPasswordByEmail(email);
    if (storedHashedPassword == null) {
        throw new RuntimeException("Invalid email or password");
    }

    try {
        boolean isValid = PasswordUtil.verifyPassword(password, storedHashedPassword);
        if (!isValid) {
            throw new RuntimeException("Invalid email or password");
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to verify password", e);
    }

    int userId = repository.login(email);
    try {
        System.out.println("User with ID: " + userId + " successfully logged in.");
        return jwtUtilRSA.createToken(userId);
    } catch (Exception e) {
        throw new RuntimeException("Failed to generate token", e);
    }
}
    
}