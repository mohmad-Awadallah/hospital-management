package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabResultDTO {
    private Long id;
    private String testName;
    private String result;
    private String date;
    private String status;
    private String notes;
    private Long patientId;      // ID المريض
    private String patientName;  // اسم المريض
    private Long doctorId;       // ID الطبيب
    private String doctorName;   // اسم الطبيب
}
