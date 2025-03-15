// src/main/java/com/hospital/exception/DuplicateEntryException.java
package com.hospital.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
