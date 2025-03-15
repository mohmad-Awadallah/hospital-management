package com.hospital.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalRecordDTO {
    private LocalDate recordDate;
    private String diagnosis;
    private String treatment;
    private Long patientId;
    private Long doctorId;


}
