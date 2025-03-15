package com.hospital.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReceptionDTO {
    private Long id;
    private Long patientId;       // ID المريض
    private String patientName;   // اسم المريض
    private Long doctorId;        // ID الطبيب
    private String doctorName;    // اسم الطبيب
    private LocalDateTime receptionDate;
    private String notes;
}