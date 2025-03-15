package com.hospital.service;

import com.hospital.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    public PaymentDTO getPaymentById(Long id);
    public List<PaymentDTO> getAllPayments();
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO);
    public void deletePayment(Long id);
}
