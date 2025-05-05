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
                .collect(Collectors.toMap(
                    parts -> parts[0].trim(),
                    parts -> {
                        String value = parts[1].trim();

                        // Remove surrounding quotes if present
                        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                            (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }

                        // Replace escaped newlines with actual newlines
                        value = value.replace("\\n", "\n").replace("\\r", "\r");

                        return value;
                    }
                ));

            env.forEach(System::setProperty);
        } catch (IOException e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }
    }
}
