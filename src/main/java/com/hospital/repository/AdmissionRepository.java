package com.hospital.repository;

import com.hospital.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    // يمكنك إضافة استعلامات مخصصة هنا إذا لزم الأمر
}