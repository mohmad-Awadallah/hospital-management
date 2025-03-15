package com.hospital.service;

import com.hospital.dto.AdmissionDTO;
import com.hospital.model.Admission;

import java.util.List;

public interface AdmissionService {
    Admission createAdmission(Admission admission);
    List<Admission> getAllAdmissions();
    Admission getAdmissionById(Long id);
    Admission updateAdmission(Long id, Admission admission);
    void deleteAdmission(Long id);
    AdmissionDTO convertToAdmissionDTO(Admission admission);
    AdmissionDTO convertToAdmissionDTO(Long id);
}