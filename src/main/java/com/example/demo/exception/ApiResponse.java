package com.example.demo.exception;

public class ApiResponse {
    private boolean success;
    private String message;
    private String exceptionType;

    public ApiResponse(boolean success, String message, String exceptionType) {
        this.success = success;
        this.message = message;
        this.exceptionType = exceptionType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
