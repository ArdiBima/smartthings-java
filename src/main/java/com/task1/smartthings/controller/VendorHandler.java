package com.task1.smartthings.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.dto.UpdateVendorDevice;
import com.task1.smartthings.model.dto.VendorDevices;
import com.task1.smartthings.service.VendorService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import org.slf4j.Logger;

public class VendorHandler implements Handler {

    private final VendorService vendorService;
    private final ObjectMapper mapper;

    public VendorHandler(VendorService vendorService, ObjectMapper mapper) {
        this.vendorService = vendorService;
        this.mapper = mapper;
    }

    @Override
    public void handle(Context ctx) {
        // Promise.sync(vendorService::listVendorDevices)
        //     .map(mapper::writeValueAsString)
        //     .then(json -> ctx.getResponse()
        //         .contentType("application/json")
        //         .send(json));
    }
    public void listVendorDevices(Context ctx) {
        String vendorIdParam = ctx.getRequest().getQueryParams().get("vendorId");
        Logger logger = org.slf4j.LoggerFactory.getLogger(VendorHandler.class);
        logger.info("Listing devices for vendorId={}", vendorIdParam);
        if (vendorIdParam == null) {
            throw new RuntimeException("Missing 'vendorId' query parameter");
        }
    
        int vendorId;
        try {
            vendorId = Integer.parseInt(vendorIdParam);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid 'vendorId' format", e);
        }
    
        List<VendorDevices> listDevices;
        try {
            listDevices = vendorService.listVendorDevices(ctx, vendorId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to list devices", e);
        }
    
        ApiResponse successResponse = new ApiResponse("Success Get List Device", true, listDevices);
        try {
            ctx.getResponse()
               .contentType("application/json")
               .send(mapper.writeValueAsString(successResponse));
        } catch (Exception e) {
            ctx.getResponse().status(500).send("Error serializing response");
        }
    }


    public void createDevice(Context ctx) {
    ctx.parse(VendorDevices.class).then(vendorDevice -> {
        vendorService.addDevice(ctx,vendorDevice);
        
        ApiResponse successResponse = new ApiResponse("Success Insert Device", true, null);
        ctx.getResponse().contentType("application/json");
        ctx.getResponse().send(mapper.writeValueAsString(successResponse));
        });
    }
    public void updateDevice(Context ctx) {
        ctx.parse(UpdateVendorDevice.class).then(updateDevice -> {
            String idParam = ctx.getRequest().getQueryParams().get("deviceId");
            String vendorId = ctx.getRequest().getQueryParams().get("vendorId");
            if (idParam == null|| vendorId == null) {
                throw new RuntimeException("Missing 'id' or 'vendorId' query parameter");
            }
            int id;
            int vendorIdInt;
            try {
                id = Integer.parseInt(idParam);
                vendorIdInt = Integer.parseInt(vendorId);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'id' or 'vendorId' format", e);
            }
            
            vendorService.updateVendorDevice(id, vendorIdInt, updateDevice);
            ApiResponse successResponse = new ApiResponse("Success Update Device", true, null);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
            });
        }
    public void deleteDevice(Context ctx) {
        
            String idParam = ctx.getRequest().getQueryParams().get("deviceId");
            String vendorId = ctx.getRequest().getQueryParams().get("vendorId");
            if (idParam == null|| vendorId == null) {
                throw new RuntimeException("Missing 'id' or 'vendorId' query parameter");
            }
            int id;
            int vendorIdInt;
            try {
                id = Integer.parseInt(idParam);
                vendorIdInt = Integer.parseInt(vendorId);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid 'id' or 'vendorId' format", e);
            }
            vendorService.deleteDevice(id,vendorIdInt);
            ApiResponse successResponse = new ApiResponse("Success Delete Device", true, null);
            try {
                ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
            } catch (Exception e) {
                ctx.getResponse().status(500).send("Error serializing response");
            }
    };
        
}
