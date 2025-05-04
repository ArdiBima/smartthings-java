package com.task1.smartthings.config;


import ratpack.handling.Context;
import ratpack.error.ClientErrorHandler;
import ratpack.error.ServerErrorHandler;

import java.util.Map;

import static ratpack.jackson.Jackson.json;

public class ErrorHandler implements ServerErrorHandler, ClientErrorHandler {

    @Override
    public void error(Context ctx, int statusCode) throws Exception {
        ctx.getResponse().status(statusCode);
        ctx.render(json(Map.of(
            "success", false,
            "message", "Client Error: " + statusCode
        )));
    }

    @Override
    public void error(Context ctx, Throwable throwable) throws Exception {
        throwable.printStackTrace(); // or use logger
        ctx.getResponse().status(500);
        ctx.render(json(Map.of(
            "success", false,
            "message", "Internal Server Error: " + throwable.getMessage()
        )));
    }
}
