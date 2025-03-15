package com.hospital.response; // أو الحزمة التي تستخدمها

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

    private String timestamp;
    private String message;
    private String status;
    private Set<String> roles;
}