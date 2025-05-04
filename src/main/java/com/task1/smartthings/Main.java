package com.task1.smartthings;

import com.task1.smartthings.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.server.RatpackServer;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting application...");
        EnvConfig.load("local.env");

        var dataSource = DBConfig.getDataSource();

        RatpackServer server = RatpackServer.of(s -> s
            .registryOf(registry -> registry
                .add(dataSource)
                .add(new ErrorHandler())
            )
            .handlers(Router.setupRoutes(dataSource))
        );

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("Shutting down gracefully...");
                server.stop();
                DBConfig.closeDataSource();
                logger.info("Shutdown complete.");
            } catch (Exception e) {
                logger.error("Error during shutdown", e);
            }
        }));

        server.start();
    }
}
