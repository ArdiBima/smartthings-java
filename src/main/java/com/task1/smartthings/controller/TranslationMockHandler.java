package com.task1.smartthings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task1.smartthings.config.ApiResponse;
import com.task1.smartthings.model.dto.TranslateSingleDto;
import com.task1.smartthings.service.TranslationService;

import java.util.List;
import java.util.Map;
import ratpack.handling.Context;
import ratpack.handling.Handler;

public class TranslationMockHandler implements Handler {
    private final TranslationService translationService;
    private final ObjectMapper mapper;
    public TranslationMockHandler(TranslationService translationService,ObjectMapper mapper) {
        this.translationService = translationService;
        this.mapper = mapper;
    }
    @Override
    public void handle(Context ctx) throws Exception {
        ctx.render("Mock translation service endpoint");
    }

    public void mockTranslateSingle(Context ctx) throws Exception{
    ctx.parse(TranslateSingleDto.class).then(dto -> {

        String translatedText = translationService.translateSingleText(dto.getText(), dto.getCountry());
        Map<String, String> mapped = Map.of("translation", translatedText);
        ApiResponse successResponse = new ApiResponse("Success translate", true, mapped);
        ctx.getResponse()
            .contentType("application/json")
            .send(mapper.writeValueAsString(successResponse));
    });
    }

    public void mockTranslateAll(Context ctx) throws Exception{
        ctx.parse(TranslateSingleDto.class).then(dto -> {
    
            List<Map<String, String>> translatedText = translationService.translateAllText(dto.getText());
            ApiResponse successResponse = new ApiResponse("Success translate", true, translatedText);
            ctx.getResponse()
                .contentType("application/json")
                .send(mapper.writeValueAsString(successResponse));
        });
        }
}