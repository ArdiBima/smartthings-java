package com.task1.smartthings.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.dto.UserDetail;
import com.task1.smartthings.service.AdminService;

import ratpack.handling.Context;
import ratpack.handling.Handler;
import org.slf4j.Logger;
public class AdminHandler implements Handler {
    private final AdminService adminService;
    private final ObjectMapper mapper;
    public AdminHandler(AdminService adminService, ObjectMapper mapper) {
            this.adminService = adminService;
            this.mapper = mapper;
        }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.render("Admin endpoint");
    }
   public void userDetail(Context ctx) {
       
        String userIdParam = ctx.getRequest().getQueryParams().get("userId");
        if (userIdParam == null) {
           throw new IllegalArgumentException("Missing 'vendor_id' query parameter");
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid 'vendor_id' format");
        }
        UserDetail userDetail;
        try{
            userDetail=adminService.UserDetail(ctx, userId);
        }catch(Exception e){
            throw new RuntimeException("Failed to list devices", e);
        }
        
        ApiResponse successResponse = new ApiResponse("Success Get User Detail", true, userDetail);
        Logger logger = org.slf4j.LoggerFactory.getLogger(AdminHandler.class);
        logger.info("User detail retrieved successfully for userId={}", userId);

        try {
            ctx.getResponse()
            .contentType("application/json")
            .send(mapper.writeValueAsString(successResponse));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.getResponse().status(500).send("Error serializing response");
        }
    };
    
    public void getVendorDeviceStats(Context ctx) {
        
            var stats=adminService.getVendorDeviceStats();
            ApiResponse successResponse = new ApiResponse("Success Get Stats", true, stats);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.getResponse().status(500).send("Error serializing response");
            }
        
        
    }

    public void getUserDeviceStats(Context ctx) {
       
            var stats=adminService.getUserDeviceStats();
            ApiResponse successResponse = new ApiResponse("Success Get Stats", true, stats);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                e.printStackTrace();
                ctx.getResponse().status(500).send("Error serializing response");
            }
        
    }
}