package com.hospital.service.impl;

import com.hospital.dto.PrescriptionDTO;
import com.hospital.model.Prescription;
import com.hospital.model.MedicalRecord;
import com.hospital.repository.PrescriptionRepository;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.service.PrescriptionService;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        // البحث عن السجل الطبي المرتبط
        MedicalRecord medicalRecord = medicalRecordRepository.findById(prescriptionDTO.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + prescriptionDTO.getMedicalRecordId()));

        // إنشاء كائن Prescription باستخدام Builder
        Prescription prescription = Prescription.builder()
                .medication(prescriptionDTO.getMedication())
                .dosage(prescriptionDTO.getDosage())
                .instructions(prescriptionDTO.getInstructions())
                .medicalRecord(medicalRecord)
                .build();

        // حفظ الوصفة الطبية في قاعدة البيانات
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // تحويل الكيان المحفوظ إلى DTO وإرجاعه
        return convertToPrescriptionDTO(savedPrescription);
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionDTO getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return convertToPrescriptionDTO(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

        // تحديث الحقول
        prescription.setMedication(prescriptionDTO.getMedication());
        prescription.setDosage(prescriptionDTO.getDosage());
        prescription.setInstructions(prescriptionDTO.getInstructions());

        // تحديث السجل الطبي إذا تم تغييره
        if (!prescription.getMedicalRecord().getId().equals(prescriptionDTO.getMedicalRecordId())) {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(prescriptionDTO.getMedicalRecordId())
                    .orElseThrow(() -> new ResourceNotFoundException("MedicalRecord not found with id: " + prescriptionDTO.getMedicalRecordId()));
            prescription.setMedicalRecord(medicalRecord);
        }

        // حفظ التحديثات
        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return convertToPrescriptionDTO(updatedPrescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        prescriptionRepository.delete(prescription);
    }

    // تحويل كيان Prescription إلى PrescriptionDTO
    private PrescriptionDTO convertToPrescriptionDTO(Prescription prescription) {
        return PrescriptionDTO.builder()
                .id(prescription.getId())
                .medication(prescription.getMedication())
                .dosage(prescription.getDosage())
                .instructions(prescription.getInstructions())
                .medicalRecordId(prescription.getMedicalRecord().getId())
                .build();
    }
}