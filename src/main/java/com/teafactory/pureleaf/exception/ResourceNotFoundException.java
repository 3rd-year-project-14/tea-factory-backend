package com.teafactory.pureleaf.exception;

// Custom exception for resource not found scenarios
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

