package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "تاريخ الموعد مطلوب")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "حالة الموعد مطلوبة")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @NotNull(message = "يجب تحديد المريض")
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "يجب تحديد الطبيب")
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}