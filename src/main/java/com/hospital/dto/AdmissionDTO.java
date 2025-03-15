package com.hospital.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmissionDTO {
    private Long id;
    private LocalDate admissionDate; // تاريخ القبول
    private LocalDate dischargeDate; // تاريخ الخروج
    private String reason; // سبب القبول
    private Long patientId; // معرف المريض
    private String patientName; // اسم المريض
    private Long doctorId; // معرف الطبيب
    private String doctorName; // اسم الطبيب
    private Long roomId; // معرف الغرفة
    private String roomNumber; // رقم الغرفة
}