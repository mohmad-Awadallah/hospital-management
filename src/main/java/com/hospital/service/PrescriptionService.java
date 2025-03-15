package com.hospital.service;

import com.hospital.dto.PrescriptionDTO;

import java.util.List;

public interface PrescriptionService {
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO);
    public PrescriptionDTO getPrescriptionById(Long id);
    public List<PrescriptionDTO> getAllPrescriptions();
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO);
    public void deletePrescription(Long id);
}
