package com.hospital.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String referenceNumber;
    private String notes;
    private Long billId; // معرف الفاتورة
}