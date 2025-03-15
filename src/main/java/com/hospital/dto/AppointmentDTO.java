package com.hospital.dto;

import com.hospital.model.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate appointmentDate; // تاريخ الموعد
    private AppointmentStatus status; // حالة الموعد
    private Long patientId; // معرف المريض
    private String patientName; // اسم المريض
    private Long doctorId; // معرف الطبيب
    private String doctorName; // اسم الطبيب
}