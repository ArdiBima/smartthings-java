package com.task1.smartthings.config;

import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.Map;

public class Middleware implements Handler {

    @Override
    public void handle(Context ctx) {
        System.out.println("Incoming request: " + ctx.getRequest().getPath());

        // Wrap the next handlers and catch any exceptions
        ctx.insert((Context innerCtx) -> {
            try {
                innerCtx.next();
            } catch (Exception e) {
                handleException(innerCtx, e);
            }
        });
    }

    private void handleException(Context ctx, Throwable e) {
        e.printStackTrace(); // Or use a logger
        ctx.getResponse().contentType("application/json");
        ctx.getResponse().status(500);
        ctx.render(ratpack.jackson.Jackson.json(Map.of(
            "success", false,
            "message", "Internal Server Error: " + e.getMessage()
        )));
    }
}
