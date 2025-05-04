package com.task1.smartthings.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class EnvConfig {
    public static void load(String filePath) {
        try {
            Map<String, String> env = Files.lines(Paths.get(filePath))
                .filter(line -> !line.startsWith("#") && line.contains("="))
                .map(line -> line.split("=", 2))
                .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1].trim()));

            // Set as system properties
            env.forEach((key, value) -> System.setProperty(key, value));
        } catch (IOException e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
}
