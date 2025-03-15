// src/main/java/com/hospital/exception/ResourceNotFoundException.java
package com.hospital.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}