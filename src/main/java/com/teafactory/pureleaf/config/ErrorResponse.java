package com.teafactory.pureleaf.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ErrorResponse is a DTO for sending structured error details in API responses.
// It is used by GlobalExceptionHandler and controllers to provide consistent error information.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;
    private String path; // optional: API path where error occurred
}
