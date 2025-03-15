package com.hospital.controller;

import com.hospital.model.Doctor;
import com.hospital.model.MedicalRecord;
import com.hospital.model.Patient;
import com.hospital.service.MedicalRecordService;
import com.hospital.dto.MedicalRecordDTO;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // إنشاء سجل طبي جديد
    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        logger.debug("طلب إنشاء سجل طبي: {}", medicalRecordDTO);

        // التحقق من وجود المريض
        Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("المريض غير موجود"));

        // التحقق من وجود الطبيب
        Doctor doctor = doctorRepository.findById(medicalRecordDTO.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("الطبيب غير موجود"));

        // إنشاء السجل الطبي باستخدام Builder
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .recordDate(medicalRecordDTO.getRecordDate())
                .diagnosis(medicalRecordDTO.getDiagnosis())
                .treatment(medicalRecordDTO.getTreatment())
                .patient(patient)
                .doctor(doctor)
                .build();

        MedicalRecord createdRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        logger.info("تم إنشاء سجل طبي بنجاح: {}", createdRecord);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    // الحصول على جميع السجلات الطبية
    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // الحصول على سجل طبي بواسطة ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecord record = medicalRecordService.getMedicalRecordById(id);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    // تحديث سجل طبي
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        logger.debug("طلب تحديث سجل طبي: {}", medicalRecordDTO);

        // التحقق من وجود السجل الطبي
        MedicalRecord existingRecord = medicalRecordService.getMedicalRecordById(id);

        // التحقق من وجود المريض
        Patient patient = patientRepository.findById(medicalRecordDTO.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("المريض غير موجود"));

        // التحقق من وجود الطبيب
        Doctor doctor = doctorRepository.findById(medicalRecordDTO.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("الطبيب غير موجود"));

        // تحديث الحقول
        existingRecord.setRecordDate(medicalRecordDTO.getRecordDate());
        existingRecord.setDiagnosis(medicalRecordDTO.getDiagnosis());
        existingRecord.setTreatment(medicalRecordDTO.getTreatment());
        existingRecord.setPatient(patient);
        existingRecord.setDoctor(doctor);

        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(id, existingRecord);
        logger.info("تم تحديث السجل الطبي بنجاح: {}", updatedRecord);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    // حذف سجل طبي
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}