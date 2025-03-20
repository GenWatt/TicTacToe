package com.adrian.ddd.api.errors;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private Integer statusCode;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }
}
