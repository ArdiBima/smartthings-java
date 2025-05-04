package com.task1.smartthings.model.dto;

public class TranslateSingleDto {
    public String text;
    public String country;
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
}
