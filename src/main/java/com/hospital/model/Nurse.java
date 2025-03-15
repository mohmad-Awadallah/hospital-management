package com.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // اسم الممرض
    private String phone; // رقم الهاتف
    private String address; // العنوان
    private LocalDate birthDate; // تاريخ الميلاد
    private String gender; // الجنس
    private LocalDate hireDate; // تاريخ التوظيف

    // علاقة مع القسم (Department)
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
