package com.task1.smartthings.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.User;
import com.task1.smartthings.model.dto.AdminUserDeviceStats;
import com.task1.smartthings.model.dto.AdminVendorDeviceStats;
import com.task1.smartthings.service.AdminService;

import ratpack.handling.Context;
import ratpack.handling.Handler;

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
        ctx.parse(int.class).then(admin -> {
        String userIdParam = ctx.getRequest().getQueryParams().get("userId");
        if (userIdParam == null) {
            ctx.getResponse().status(400).send("Missing 'vendor_id' query parameter");
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdParam);
        } catch (NumberFormatException e) {
            ctx.getResponse().status(400).send("Invalid 'vendor_id' format");
            return;
        }
        User userDetail;
        try{
            userDetail=adminService.UserDetail(ctx, userId);
        }catch(Exception e){
            ctx.getResponse().status(500).send("Internal Server Error");
            return;
        }
        
        ApiResponse successResponse = new ApiResponse("Success Get List Device", true, userDetail);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
        });
    }
    public void getVendorDeviceStats(Context ctx) {
        ctx.parse(AdminVendorDeviceStats.class).then(adminVendorStats -> {
            var stats=adminService.getVendorDeviceStats();
            ApiResponse successResponse = new ApiResponse("Success Get Stats", true, stats);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
        });
        
    }

    public void getUserDeviceStats(Context ctx) {
        ctx.parse(AdminUserDeviceStats.class).then(adminUserStats -> {
            var stats=adminService.getUserDeviceStats();
            ApiResponse successResponse = new ApiResponse("Success Get Stats", true, stats);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
        });
    }
}