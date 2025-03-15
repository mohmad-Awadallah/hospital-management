package com.hospital.repository;

import com.hospital.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // البحث عن القسم عن طريق الاسم
    Optional<Department> findByName(String name);
}