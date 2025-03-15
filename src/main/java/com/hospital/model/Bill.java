package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "المبلغ مطلوب")
    @DecimalMin(value = "0.0", inclusive = false, message = "يجب أن يكون المبلغ أكبر من الصفر")
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull(message = "تاريخ الفاتورة مطلوب")
    @PastOrPresent(message = "تاريخ الفاتورة يجب أن يكون في الماضي أو الحاضر")
    private LocalDate billDate;

    @NotBlank(message = "وصف الفاتورة مطلوب")
    @Size(max = 500, message = "الوصف يجب ألا يتجاوز 500 حرف")
    private String description;

    @NotNull(message = "يجب تحديد المريض")
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "يجب تحديد القبول")
    @ManyToOne(optional = false)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();
}