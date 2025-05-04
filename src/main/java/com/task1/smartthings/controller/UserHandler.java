package com.task1.smartthings.controller;

import java.util.HashMap;
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
        ctx.parse(String.class).then(user -> {
            String userIdParam = ctx.getRequest().getQueryParams().get("userId");
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");

            if (userIdParam == null || deviceIdParam == null) {
                ctx.getResponse().status(400).send("Missing 'userId' or 'deviceId' query parameter");
                return;
            }
    
            int userId;
            int deviceId;
            try {
                userId = Integer.parseInt(userIdParam);
                deviceId = Integer.parseInt(deviceIdParam);
            } catch (NumberFormatException e) {
                ctx.getResponse().status(400).send("Invalid 'userId' or 'deviceId' format");
                return;
            }
            userService.bindDeviceUserService(userId, deviceId);

            ApiResponse successResponse = new ApiResponse("Success Bind User Device", true, null);
            ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            
               
           
        });
    }
    public void unbindDeviceUser(Context ctx) {
        ctx.parse(String.class).then(user -> {
            String userIdParam = ctx.getRequest().getQueryParams().get("userId");
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");

            if (userIdParam == null || deviceIdParam == null) {
                ctx.getResponse().status(400).send("Missing 'userId' or 'deviceId' query parameter");
                return;
            }
    
            int userId;
            int deviceId;
            try {
                userId = Integer.parseInt(userIdParam);
                deviceId = Integer.parseInt(deviceIdParam);
            } catch (NumberFormatException e) {
                ctx.getResponse().status(400).send("Invalid 'userId' or 'deviceId' format");
                return;
            }
            userService.unbindDeviceUserService(userId, deviceId);

            ApiResponse successResponse = new ApiResponse("Success Unbind User Device", true, null);
            ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
        });
    }
    public void changeDeviceValue(Context ctx) {
        ctx.parse(String.class).then(user -> {
            String userIdParam = ctx.getRequest().getQueryParams().get("userId");
            String deviceIdParam = ctx.getRequest().getQueryParams().get("deviceId");
            String deviceValueParam = ctx.getRequest().getQueryParams().get("deviceValue");
            if (userIdParam == null || deviceIdParam == null|| deviceValueParam == null) {
                ctx.getResponse().status(400).send("Missing 'userId' or 'deviceId' or 'deviceValue' query parameter");
                return;
            }
    
            int userId;
            int deviceId;
            int deviceValue;
            try {
                userId = Integer.parseInt(userIdParam);
                deviceId = Integer.parseInt(deviceIdParam);
                deviceValue = Integer.parseInt(deviceValueParam);
            } catch (NumberFormatException e) {
                ctx.getResponse().status(400).send("Invalid 'userId' or 'deviceId' or 'deviceValue' format");
                return;
            }
            userService.updateDeviceValueById(userId, deviceId, deviceValue);

            ApiResponse successResponse = new ApiResponse("Success Update User Device Value", true, null);
            ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
        });
    }
}
