package com.hospital.repository;

import com.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findById(Long id);
    boolean existsById(Long id);
    Optional<Patient> findByPhone(String phone); // للتحقق من وجود رقم الهاتف
}