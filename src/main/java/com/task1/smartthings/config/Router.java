package com.task1.smartthings.config;

import com.task1.smartthings.controller.AdminHandler;
import com.task1.smartthings.controller.UserHandler;
import com.task1.smartthings.controller.VendorHandler;
import com.task1.smartthings.repository.AdminRepository;
import com.task1.smartthings.repository.VendorRepository;
import com.task1.smartthings.repository.UserRepository;
import com.task1.smartthings.service.UserService;
import com.task1.smartthings.service.AdminService;
import com.task1.smartthings.service.VendorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import ratpack.func.Action;
import ratpack.handling.Chain;

public class Router {
    public static Action<Chain> setupRoutes(DataSource dataSource) {
        return chain -> chain
            .all(new Middleware())
            .prefix("v1/smartthings", smartthings -> {
                ObjectMapper mapper = new ObjectMapper();

                // Devices routes
                smartthings.prefix("admin", deviceChain -> {
                    AdminRepository deviceRepo = new AdminRepository(dataSource);
                    AdminService deviceService = new AdminService(deviceRepo);
                    AdminHandler adminHandler = new AdminHandler(deviceService, mapper);
                    deviceChain.get("user/detail", adminHandler::userDetail);
                    deviceChain.get("list/user", adminHandler::getUserDeviceStats);
                    deviceChain.get("list/vendor", adminHandler::getVendorDeviceStats);
                });

                // Users routes
                smartthings.prefix("users", userChain -> {
                    UserRepository userRepo = new UserRepository(dataSource);
                    UserService userService = new UserService(userRepo);
                    UserHandler userHandler = new UserHandler(userService, mapper);
                    userChain.post("register", userHandler::registerUser);
                    userChain.post("device/bind", userHandler::bindDeviceUser);
                    userChain.post("device/unbind", userHandler::unbindDeviceUser);
                    userChain.post("device/control", userHandler::changeDeviceValue);
                });

                // Vendors routes
                smartthings.prefix("vendors", vendorChain -> {
                    VendorRepository vendorRepo = new VendorRepository(dataSource);
                    VendorService vendorService = new VendorService(vendorRepo);
                    VendorHandler vendorHandler = new VendorHandler(vendorService, mapper);
                    vendorChain.get("devices/list", vendorHandler::handle);
                    vendorChain.post("device/add", vendorHandler::createDevice);
                    vendorChain.post("device/update", vendorHandler::updateDevice);
                    vendorChain.delete("device/delete", vendorHandler::deleteDevice);
                });
            });
    }
}
