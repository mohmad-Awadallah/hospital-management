package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor // مُنشئ بدون وسائط
@AllArgsConstructor // مُنشئ مع جميع الحقول
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate paymentDate; // تاريخ الدفع

    @NotNull
    private BigDecimal amount; // المبلغ المدفوع

    @NotNull
    private String paymentMethod; // طريقة الدفع (نقدي، بطاقة ائتمان، تحويل بنكي)

    @NotNull
    private String paymentStatus; // حالة الدفع (مكتمل، معلق، فشل)

    private String referenceNumber; // رقم المرجع (مثل رقم المعاملة البنكية)

    private String notes; // ملاحظات إضافية

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill; // العلاقة مع الفاتورة
}