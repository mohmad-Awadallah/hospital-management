package com.hospital.service.impl;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.model.Patient;
import com.hospital.repository.PatientRepository;
import com.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public Patient addPatient(Patient patient) {
        log.info("Adding new patient: {}", patient.getName());

        // التحقق من وجود رقم الهاتف مسبقًا
        if (patientRepository.findByPhone(patient.getPhone()).isPresent()) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        return patientRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> getPatient(Long id) {
        log.info("Fetching patient with ID: {}", id);
        return patientRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Patient> updatePatient(Long id, Patient patient) {
        log.info("Updating patient with ID: {}", id);

        return patientRepository.findById(id)
                .map(existingPatient -> {
                    // التحقق من وجود رقم الهاتف مسبقًا (إذا تم تغييره)
                    if (!existingPatient.getPhone().equals(patient.getPhone())
                            && patientRepository.findByPhone(patient.getPhone()).isPresent()) {
                        throw new DuplicateResourceException("Phone number already exists.");
                    }

                    existingPatient.setName(patient.getName());
                    existingPatient.setGender(patient.getGender());
                    existingPatient.setBirthDate(patient.getBirthDate());
                    existingPatient.setPhone(patient.getPhone());
                    existingPatient.setContactNumber(patient.getContactNumber());
                    existingPatient.setAddress(patient.getAddress());

                    return patientRepository.save(existingPatient);
                });
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        patientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }
}