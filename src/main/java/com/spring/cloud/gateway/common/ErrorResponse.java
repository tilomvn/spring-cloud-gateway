package com.spring.cloud.gateway.common;

public class ErrorResponse {
    int code;
    String message;

    public ErrorResponse(int code, String message) { this.code = code; this.message = message; }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}