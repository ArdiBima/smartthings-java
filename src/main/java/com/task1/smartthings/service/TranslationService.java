package com.task1.smartthings.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TranslationService {
    public String translateSingleText(String text, String country) {
        // Rest of your code logic
        String normalizedText = text.toLowerCase();
        String normalizedCountry = country.toLowerCase();
    
        return switch (normalizedCountry) {
            
            case "germany" -> switch (normalizedText) {
                case "lightbulb" -> "Lampe";
                case "fan" -> "Lüfter";
                case "air conditioner" -> "Klimaanlage";
                case "tv" -> "Fernseher";
                case "refrigerator" -> "Kühlbox";
                case "washer" -> "Waschmaschine";
                case "rice cooker" -> "Reiskocher";
                default -> text;
            };
            case "france" -> switch (normalizedText) {
                case "lightbulb" -> "Ampoule";
                case "fan" -> "Ventilateur";
                case "air conditioner" -> "Climatiseur";
                case "tv" -> "Télévision";
                case "refrigerator" -> "Réfrigérateur";
                case "washer" -> "Lave-linge";
                case "rice cooker" -> "Cuiseur à riz";
                default -> text;
            };
            default -> text;
        };
    }
    public List<Map<String, String>> translateAllText(String text) {
        String normalizedText = text.toLowerCase();

        List<Map<String, String>> translations = new ArrayList<>();

        Map<String, String> germany = Map.of(
            "country", "Germany",
            "translation", switch (normalizedText) {
                case "lightbulb" -> "Lampe";
                case "fan" -> "Lüfter";
                case "air conditioner" -> "Klimaanlage";
                case "tv" -> "Fernseher";
                case "refrigerator" -> "Kühlbox";
                case "washer" -> "Waschmaschine";
                case "rice cooker" -> "Reiskocher";
                default -> text;
            }
        );

        Map<String, String> france = Map.of(
            "country", "France",
            "translation", switch (normalizedText) {
                case "lightbulb" -> "Ampoule";
                case "fan" -> "Ventilateur";
                case "air conditioner" -> "Climatiseur";
                case "tv" -> "Télévision";
                case "refrigerator" -> "Réfrigérateur";
                case "washer" -> "Lave-linge";
                case "rice cooker" -> "Cuiseur à riz";
                default -> text;
            }
        );

        // translations.add(japan);
        translations.add(germany);
        translations.add(france);

        return translations;
    }
}