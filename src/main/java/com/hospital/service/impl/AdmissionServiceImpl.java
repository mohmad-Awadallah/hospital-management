package com.hospital.service.impl;

import com.hospital.dto.AdmissionDTO;
import com.hospital.model.Admission;
import com.hospital.repository.AdmissionRepository;
import com.hospital.service.AdmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class AdmissionServiceImpl implements AdmissionService {

    private static final Logger logger = LoggerFactory.getLogger(AdmissionServiceImpl.class);

    @Autowired
    private AdmissionRepository admissionRepository;

    @Override
    public Admission createAdmission(Admission admission) {
        logger.debug("إنشاء قبول جديد: {}", admission);
        return admissionRepository.save(admission);
    }

    @Override
    public List<Admission> getAllAdmissions() {
        logger.debug("جلب جميع القبولات");
        return admissionRepository.findAll();
    }

    @Override
    public Admission getAdmissionById(Long id) {
        logger.debug("جلب القبول بواسطة ID: {}", id);
        return admissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("القبول غير موجود"));
    }

    @Override
    public Admission updateAdmission(Long id, Admission admission) {
        logger.debug("تحديث القبول: {}", admission);
        Admission existingAdmission = getAdmissionById(id);
        existingAdmission.setAdmissionDate(admission.getAdmissionDate());
        existingAdmission.setDischargeDate(admission.getDischargeDate());
        existingAdmission.setReason(admission.getReason());
        existingAdmission.setPatient(admission.getPatient());
        existingAdmission.setDoctor(admission.getDoctor());
        existingAdmission.setRoom(admission.getRoom());
        return admissionRepository.save(existingAdmission);
    }

    @Override
    public void deleteAdmission(Long id) {
        logger.debug("حذف القبول بواسطة ID: {}", id);
        admissionRepository.deleteById(id);
    }

    @Override
    public AdmissionDTO convertToAdmissionDTO(Admission admission) {
        AdmissionDTO admissionDTO = new AdmissionDTO();
        admissionDTO.setId(admission.getId());
        admissionDTO.setAdmissionDate(admission.getAdmissionDate());
        admissionDTO.setDischargeDate(admission.getDischargeDate());
        admissionDTO.setReason(admission.getReason());
        admissionDTO.setPatientId(admission.getPatient().getId());
        admissionDTO.setPatientName(admission.getPatient().getName());
        admissionDTO.setDoctorId(admission.getDoctor().getId());
        admissionDTO.setDoctorName(admission.getDoctor().getName());
        admissionDTO.setRoomId(admission.getRoom().getId());
        admissionDTO.setRoomNumber(admission.getRoom().getRoomNumber());
        return admissionDTO;
    }

    @Override
    public AdmissionDTO convertToAdmissionDTO(Long id) {
        Admission admission = getAdmissionById(id);
        return convertToAdmissionDTO(admission);
    }
}