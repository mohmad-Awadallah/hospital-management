package com.hospital.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    private Long id;
    private String medication; // الدواء
    private String dosage; // الجرعة
    private String instructions; // التعليمات
    private Long medicalRecordId; // معرف السجل الطبي
}