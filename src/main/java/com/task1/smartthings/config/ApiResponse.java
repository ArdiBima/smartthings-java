package com.task1.smartthings.config;

public class ApiResponse {
    private String message;
    private boolean success;
    private Object data;  // Add a field to hold any response data

    // Constructor
    public ApiResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
        
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

