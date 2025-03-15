package com.hospital.exception;

import lombok.Data;
import java.util.Date;

@Data
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String errorCode; // حقل إضافي

    // الكونستركتر الأول بدون errorCode
    public ErrorDetails(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = "Unknown Error"; // تعيين قيمة افتراضية لحقل errorCode
    }

    // الكونستركتر الثاني مع errorCode
    public ErrorDetails(Date timestamp, String message, String errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = errorCode;
    }
}
