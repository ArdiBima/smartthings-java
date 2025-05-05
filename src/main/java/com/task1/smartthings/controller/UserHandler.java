package com.task1.smartthings.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.dto.UserRegisterDTO;
import com.task1.smartthings.service.UserService;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class UserHandler implements Handler {
    private final UserService userService;
    private final ObjectMapper mapper;
     public UserHandler(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.render("User endpoint");
    }
    public void registerUser(Context ctx) {
    ctx.parse(UserRegisterDTO.class).then(user -> {
        int userId = userService.registerUser(user);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);

        ApiResponse successResponse = new ApiResponse("Success Register User", true, data);
        ctx.getResponse()
            .contentType("application/json")
            .send(mapper.writeValueAsString(successResponse));
        
    });
    }
    public void bindDeviceUser(Context ctx) {
       
            // 
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");

            if ( deviceIdParam == null) {
                throw new RuntimeException("Missing  'deviceId' query parameter");
            }
    
            int userId;
            int deviceId;
            try {
                userId = ctx.get(Integer.class);
                deviceId = Integer.parseInt(deviceIdParam);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'userId' or 'deviceId' format", e);
            }
            userService.bindDeviceUserService(userId, deviceId);

            ApiResponse successResponse = new ApiResponse("Success Bind User Device", true, null);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
        
        };
    
    public void unbindDeviceUser(Context ctx) {
        
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");

            if ( deviceIdParam == null) {
                throw new RuntimeException("Missing 'userId' or 'deviceId' query parameter");
            }
    
            int userId;
            int deviceId;
            try {
                userId = ctx.get(Integer.class);
                deviceId = Integer.parseInt(deviceIdParam);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'userId' or 'deviceId' format", e);
            }
            userService.unbindDeviceUserService(userId, deviceId);

            ApiResponse successResponse = new ApiResponse("Success Unbind User Device", true, null);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
        };
    
    public void changeDeviceValue(Context ctx) {
        
            
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");
            String deviceValueParam = ctx.getRequest().getQueryParams().get("deviceValue");
            if ( deviceIdParam == null|| deviceValueParam == null) {
                throw new RuntimeException("Missing 'userId' or 'deviceId' or 'deviceValue' query parameter");
            }
    
            int userId;
            int deviceId;
            int deviceValue;
            try {
                userId = ctx.get(Integer.class);
                deviceId = Integer.parseInt(deviceIdParam);
                deviceValue = Integer.parseInt(deviceValueParam);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'userId' or 'deviceId' or 'deviceValue' format", e);
            }
            userService.updateDeviceValueById(userId, deviceId, deviceValue);

            ApiResponse successResponse = new ApiResponse("Success Update User Device Value", true, null);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
        };
    
    public void getAvlilableDevices(Context ctx) {
        
            

            
            int userId;
            try {
                userId = ctx.get(Integer.class);
                
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'userId' format", e);
            }
            
            List<Map<String, Object>> devices = null;
            try {
                devices = userService.getTranslatedAvailableDevices(userId);
            } catch (Exception e) {
                throw new RuntimeException("Error getting available devices", e);
            }

            ApiResponse successResponse = new ApiResponse("Success Get Available Device", true, devices);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
        };
    
    public void getUserBindedDevices(Context ctx) {
            int userId;
            try {
                userId = ctx.get(Integer.class);
                
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'userId' format", e);
            }
            List<Map<String, Object>> devices = null;
            try {
                devices = userService.getUserBindedDevices(userId);
            } catch (Exception e) {
            }

            ApiResponse successResponse = new ApiResponse("Success Get User Devices", true, devices);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
        };
    public void login(Context ctx) {
        String email = ctx.getRequest().getQueryParams().get("email");
        String password = ctx.getRequest().getQueryParams().get("password");
        if (email == null || password == null) {
            throw new RuntimeException("Missing 'username' or 'password' query parameter");
        }
        String token = userService.login(email, password);
        ApiResponse successResponse = new ApiResponse("Success Login", true, token);
        try {
            ctx.getResponse()
            .contentType("application/json")
            .send(mapper.writeValueAsString(successResponse));
        } catch (Exception e) {
            ctx.getResponse().status(500).send("Error serializing response");
        }
    };
}
