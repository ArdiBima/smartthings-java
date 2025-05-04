package com.task1.smartthings.controller;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class TranslationMockHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        ctx.render("Mock translation service endpoint");
    }
}