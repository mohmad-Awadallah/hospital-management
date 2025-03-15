package com.hospital.service.impl;

import com.hospital.model.MedicalRecord;
import com.hospital.repository.MedicalRecordRepository;
import com.hospital.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        logger.debug("إنشاء سجل طبي: {}", medicalRecord);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("جلب جميع السجلات الطبية");
        return medicalRecordRepository.findAll();
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        logger.debug("جلب السجل الطبي بواسطة ID: {}", id);
        Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
        return record.orElseThrow(() -> new RuntimeException("السجل الطبي غير موجود"));
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecord) {
        logger.debug("تحديث السجل الطبي: {}", medicalRecord);
        MedicalRecord existingRecord = getMedicalRecordById(id);
        existingRecord.setRecordDate(medicalRecord.getRecordDate());
        existingRecord.setDiagnosis(medicalRecord.getDiagnosis());
        existingRecord.setTreatment(medicalRecord.getTreatment());
        existingRecord.setPatient(medicalRecord.getPatient());
        existingRecord.setDoctor(medicalRecord.getDoctor());
        return medicalRecordRepository.save(existingRecord);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        logger.debug("حذف السجل الطبي بواسطة ID: {}", id);
        medicalRecordRepository.deleteById(id);
    }
}
