package com.hospital.repository;

import com.hospital.model.Role;
import com.hospital.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // البحث عن دور بواسطة الاسم (RoleName)
    Optional<Role> findByName(RoleName name);

    // التحقق من وجود دور بواسطة الاسم (RoleName)
    boolean existsByName(RoleName name);
}