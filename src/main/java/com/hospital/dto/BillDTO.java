package com.hospital.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate billDate;
    private String description;
    private Long patientId;
    private Long admissionId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
