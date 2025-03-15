package com.hospital.service.impl;

import com.hospital.dto.BillDTO;
import com.hospital.model.Bill;
import com.hospital.model.Patient;
import com.hospital.model.Admission;
import com.hospital.repository.BillRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.AdmissionRepository;
import com.hospital.service.BillService;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AdmissionRepository admissionRepository;

    @Override
    @Transactional
    public BillDTO createBill(BillDTO billDTO) {
        // البحث عن المريض
        Patient patient = patientRepository.findById(billDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billDTO.getPatientId()));

        // البحث عن القبول
        Admission admission = admissionRepository.findById(billDTO.getAdmissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + billDTO.getAdmissionId()));

        // إنشاء الفاتورة
        Bill bill = new Bill();
        bill.setAmount(billDTO.getAmount());
        bill.setBillDate(billDTO.getBillDate());
        bill.setDescription(billDTO.getDescription());
        bill.setPatient(patient);
        bill.setAdmission(admission);

        // حفظ الفاتورة
        Bill savedBill = billRepository.save(bill);

        // تحويل الكيان إلى DTO
        return convertToBillDTO(savedBill);
    }

    @Override
    @Transactional(readOnly = true)
    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        return convertToBillDTO(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(this::convertToBillDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillDTO updateBill(Long id, BillDTO billDTO) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));

        // تحديث الحقول
        bill.setAmount(billDTO.getAmount());
        bill.setBillDate(billDTO.getBillDate());
        bill.setDescription(billDTO.getDescription());

        // تحديث المريض والقبول إذا تم تغييرهم
        if (!bill.getPatient().getId().equals(billDTO.getPatientId())) {
            Patient patient = patientRepository.findById(billDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + billDTO.getPatientId()));
            bill.setPatient(patient);
        }

        if (!bill.getAdmission().getId().equals(billDTO.getAdmissionId())) {
            Admission admission = admissionRepository.findById(billDTO.getAdmissionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + billDTO.getAdmissionId()));
            bill.setAdmission(admission);
        }

        // حفظ التحديثات
        Bill updatedBill = billRepository.save(bill);
        return convertToBillDTO(updatedBill);
    }

    @Override
    @Transactional
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        billRepository.delete(bill);
    }

    // تحويل كيان Bill إلى BillDTO
    private BillDTO convertToBillDTO(Bill bill) {
        return BillDTO.builder()
                .id(bill.getId())
                .amount(bill.getAmount())
                .billDate(bill.getBillDate())
                .description(bill.getDescription())
                .patientId(bill.getPatient().getId())
                .admissionId(bill.getAdmission().getId())
                .createdAt(bill.getCreatedAt())
                .updatedAt(bill.getUpdatedAt())
                .build();
    }
}