package com.hospital.service.impl;

import com.hospital.dto.PaymentDTO;
import com.hospital.model.Payment;
import com.hospital.model.Bill;
import com.hospital.repository.PaymentRepository;
import com.hospital.repository.BillRepository;
import com.hospital.service.PaymentService;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        // تحويل DTO إلى كيان Payment
        Payment payment = new Payment();
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setReferenceNumber(paymentDTO.getReferenceNumber());
        payment.setNotes(paymentDTO.getNotes());

        // البحث عن الفاتورة المرتبطة
        Bill bill = billRepository.findById(paymentDTO.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + paymentDTO.getBillId()));
        payment.setBill(bill);

        // حفظ الدفع في قاعدة البيانات
        Payment savedPayment = paymentRepository.save(payment);

        // تحويل الكيان المحفوظ إلى DTO وإرجاعه
        return convertToPaymentDTO(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return convertToPaymentDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        // تحديث الحقول
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setReferenceNumber(paymentDTO.getReferenceNumber());
        payment.setNotes(paymentDTO.getNotes());

        // تحديث الفاتورة إذا تم تغييرها
        if (!payment.getBill().getId().equals(paymentDTO.getBillId())) {
            Bill bill = billRepository.findById(paymentDTO.getBillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + paymentDTO.getBillId()));
            payment.setBill(bill);
        }

        // حفظ التحديثات
        Payment updatedPayment = paymentRepository.save(payment);
        return convertToPaymentDTO(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        paymentRepository.delete(payment);
    }

    // تحويل كيان Payment إلى PaymentDTO
    private PaymentDTO convertToPaymentDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .referenceNumber(payment.getReferenceNumber())
                .notes(payment.getNotes())
                .billId(payment.getBill().getId())
                .build();
    }
}