package com.hospital.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NurseDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String gender;
    private LocalDate hireDate;
    private Long departmentId; // معرف القسم
}