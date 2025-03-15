package com.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;  // اسم الدواء
    private int quantity;         // الكمية
    private double price;         // السعر
    private String description;   // وصف الدواء
}