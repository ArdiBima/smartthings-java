package com.task1.smartthings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.service.AdminService;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.exec.Promise;

public class DeviceHandler implements Handler {

    private final AdminService deviceService;
    private final ObjectMapper mapper;

    public DeviceHandler(AdminService deviceService, ObjectMapper mapper) {
        this.deviceService = deviceService;
        this.mapper = mapper;
    }

    @Override
    public void handle(Context ctx) {
        Promise.sync(deviceService::listDevices)
            .map(mapper::writeValueAsString)
            .then(json -> ctx.getResponse()
                .contentType("application/json")
                .send(json));
    }
}
