package com.hospital.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor // مُنشئ بدون وسائط
@AllArgsConstructor // مُنشئ مع جميع الحقول
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medication; // الدواء
    private String dosage; // الجرعة
    private String instructions; // التعليمات

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord; // العلاقة مع السجل الطبي
}