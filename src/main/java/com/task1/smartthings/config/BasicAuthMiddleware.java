package com.task1.smartthings.config;

import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthMiddleware implements Handler {
    private final String username;
    private final String password;

    public BasicAuthMiddleware(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String authorizationHeader = ctx.getRequest().getHeaders().get("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            throw new IllegalArgumentException("Unauthorized: No token provided");
        }

        String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(base64Credentials);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unauthorized: Invalid credentials");
        }

        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] parts = credentials.split(":", 2);

        if (parts.length != 2 || !parts[0].equals(username) || !parts[1].equals(password)) {
           throw new IllegalArgumentException("Unauthorized: Invalid credentials");
        }

        ctx.next(); // proceed to next handler if auth passes
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
