package com.task1.smartthings.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.dto.UpdateVendorDevice;
import com.task1.smartthings.model.dto.VendorDevices;
import com.task1.smartthings.service.VendorService;
import ratpack.handling.Context;
import ratpack.handling.Handler;


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
        ctx.parse(VendorDevices.class).then(vendorDevice -> {
        String vendorIdParam = ctx.getRequest().getQueryParams().get("vendorId");
        if (vendorIdParam == null) {
            ctx.getResponse().status(400).send("Missing 'vendor_id' query parameter");
            return;
        }
        int vendorId;
        try {
            vendorId = Integer.parseInt(vendorIdParam);
        } catch (NumberFormatException e) {
            ctx.getResponse().status(400).send("Invalid 'vendor_id' format");
            return;
        }
        List<VendorDevices> listDevices;
        try{
            listDevices=vendorService.listVendorDevices(ctx, vendorId);
        }catch(Exception e){
            ctx.getResponse().status(500).send("Internal Server Error");
            return;
        }
        
        ApiResponse successResponse = new ApiResponse("Success Get List Device", true, listDevices);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
        });
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
            String idParam = ctx.getRequest().getQueryParams().get("id");
            if (idParam == null) {
                ctx.getResponse().status(400).send("Missing 'id' query parameter");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                ctx.getResponse().status(400).send("Invalid 'id' format");
                return;
            }
            vendorService.updateVendorDevice(id, updateDevice);
            ApiResponse successResponse = new ApiResponse("Success Update Device", true, null);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
            });
        }
    public void deleteDevice(Context ctx) {
        ctx.parse(String.class).then(deleteDevice -> {
            String idParam = ctx.getRequest().getQueryParams().get("id");
            if (idParam == null) {
                ctx.getResponse().status(400).send("Missing 'id' query parameter");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                ctx.getResponse().status(400).send("Invalid 'id' format");
                return;
            }
            vendorService.deleteDevice(id);
            ApiResponse successResponse = new ApiResponse("Success Delete Device", true, null);
            ctx.getResponse().contentType("application/json");
            ctx.getResponse().send(mapper.writeValueAsString(successResponse));
            });
        }    
}
