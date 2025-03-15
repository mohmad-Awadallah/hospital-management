package com.hospital.repository;

import com.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // البحث عن الأطباء عن طريق القسم
    List<Doctor> findByDepartmentId(Long departmentId);

    // البحث عن الأطباء عن طريق التخصص
    List<Doctor> findBySpecialization(String specialization);

    // البحث عن الأطباء عن طريق الاسم
    List<Doctor> findByNameContainingIgnoreCase(String name);
}