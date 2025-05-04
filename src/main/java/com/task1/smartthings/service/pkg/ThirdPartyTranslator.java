package com.task1.smartthings.service.pkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;

public class ThirdPartyTranslator {

    private static final String API_KEY = System.getProperty("TRANSLATOR_BASEURL");

    public ThirdPartyTranslator() {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException("API_KEY must be set as a system property");
        }
    }

    public String translateDeviceName(String text, String country) throws IOException {
        URL url = new URL(String.format("%s/translation/translate/single", API_KEY));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = String.format("""
            {
                "text": "%s",
                "country": "%s"
            }
        """, text, country);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
            return rootNode.path("data").path("translation").asText(text); // fallback to original if missing
        }
    }
}
