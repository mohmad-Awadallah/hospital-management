package com.hospital.dto;

import lombok.Data;

@Data
public class PharmacyDTO {
    private Long id;
    private String medicineName;  // اسم الدواء
    private int quantity;         // الكمية
    private double price;         // السعر
    private String description;   // وصف الدواء
}