package com.hospital.service;

import com.hospital.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient addPatient(Patient patient);
    Optional<Patient> getPatient(Long id);
    List<Patient> getAllPatients();
    Optional<Patient> updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
    boolean existsById(Long id);
}
