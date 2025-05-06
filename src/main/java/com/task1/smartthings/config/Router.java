package com.task1.smartthings.config;

import com.task1.smartthings.controller.AdminHandler;
import com.task1.smartthings.controller.TranslationMockHandler;
import com.task1.smartthings.controller.UserHandler;
import com.task1.smartthings.controller.VendorHandler;
import com.task1.smartthings.repository.AdminRepository;
import com.task1.smartthings.repository.VendorRepository;
import com.task1.smartthings.repository.UserRepository;
import com.task1.smartthings.service.UserService;
import com.task1.smartthings.service.AdminService;
import com.task1.smartthings.service.TranslationService;
import com.task1.smartthings.service.VendorService;
import com.task1.smartthings.service.pkg.ThirdPartyTranslator;
import com.task1.smartthings.util.JwtUtilRSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import ratpack.func.Action;
import ratpack.handling.Chain;

public class Router {
    public static Action<Chain> setupRoutes(DataSource dataSource) {
        return chain -> chain
            .all(new Middleware())
            .prefix("v1/smartthings", smartthings -> {
                JwtUtilRSA jwtUtilRSA = new JwtUtilRSA();
                ObjectMapper mapper = new ObjectMapper();
                smartthings.get("health", ctx -> ctx.getResponse().send("OK"));
                // Devices routes
                smartthings.prefix("admin", adminChain -> {
                    AdminRepository deviceRepo = new AdminRepository(dataSource);
                    AdminService deviceService = new AdminService(deviceRepo);
                    AdminHandler adminHandler = new AdminHandler(deviceService, mapper);
                    adminChain.all(new BasicAuthMiddleware(System.getProperty("ADMIN_USERNAME"), System.getProperty("ADMIN_PASSWORD")));
                    adminChain.get("user/detail", adminHandler::userDetail);
                    adminChain.get("list/user", adminHandler::getUserDeviceStats);
                    adminChain.get("list/vendor", adminHandler::getVendorDeviceStats);
                });

                // Users routes
                smartthings.prefix("users", userChain -> {
                    
                    ThirdPartyTranslator translator = new ThirdPartyTranslator();
                    UserRepository userRepo = new UserRepository(dataSource);
                    UserService userService = new UserService(userRepo, translator, mapper,jwtUtilRSA);
                    UserHandler userHandler = new UserHandler(userService, mapper);
                    userChain.post("register", userHandler::registerUser);
                    userChain.post("login", userHandler::login);
                    userChain.all(new JwtMiddleware(jwtUtilRSA));
                    userChain.post("device/bind", userHandler::bindDeviceUser);
                    userChain.post("device/unbind", userHandler::unbindDeviceUser);
                    userChain.post("device/control", userHandler::changeDeviceValue);
                    userChain.get("devices/available", userHandler::getAvlilableDevices);
                    userChain.get("devices/list", userHandler::getUserBindedDevices);
                });

                // Vendors routes
                smartthings.prefix("vendors", vendorChain -> {
                    VendorRepository vendorRepo = new VendorRepository(dataSource);
                    VendorService vendorService = new VendorService(vendorRepo);
                    VendorHandler vendorHandler = new VendorHandler(vendorService, mapper);
                    vendorChain.all(new BasicAuthMiddleware(System.getProperty("VENDOR_USERNAME"), System.getProperty("VENDOR_PASSWORD")));
                    vendorChain.get("devices/list", vendorHandler::listVendorDevices);
                    vendorChain.post("device/add", vendorHandler::createDevice);
                    vendorChain.post("device/update", vendorHandler::updateDevice);
                    vendorChain.delete("device/delete", vendorHandler::deleteDevice);
                });

                smartthings.prefix("translation", translationChain -> {
                    TranslationService translationService = new TranslationService();
                    TranslationMockHandler translationMockHandler = new TranslationMockHandler(translationService, mapper);
                    translationChain.post("translate/single", translationMockHandler::mockTranslateSingle);
                    translationChain.post("translate/all", translationMockHandler::mockTranslateAll);
                });
                
            });
    }
}
