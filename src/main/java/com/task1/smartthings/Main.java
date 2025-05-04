package com.task1.smartthings;

import org.slf4j.LoggerFactory;

import com.task1.smartthings.config.*;
import ratpack.server.RatpackServer;
import org.slf4j.Logger;
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        logger.info( "Starting application...");
        EnvConfig.load("local.env");
        RatpackServer.start(server -> server
            .registryOf(registry -> registry
                .add(DBConfig.getDataSource())
                .add(new ErrorHandler())
            )
            .handlers(Router.setupRoutes(DBConfig.getDataSource()))
        );
    }
}
