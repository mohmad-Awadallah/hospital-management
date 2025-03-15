package com.hospital.service;

import com.hospital.model.Role;
import com.hospital.model.RoleName;
import com.hospital.repository.RoleRepository;
import jakarta.annotation.PostConstruct;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class RoleInitializationService {

    private static final Logger logger = LoggerFactory.getLogger(RoleInitializationService.class);

    private final RoleRepository roleRepository;

    @PostConstruct
    @Transactional
    public void initializeRoles() {
        createRoleIfNotExists(RoleName.ROLE_PATIENT);
        createRoleIfNotExists(RoleName.ROLE_PHARMACIST);
        createRoleIfNotExists(RoleName.ROLE_ADMIN);
        createRoleIfNotExists(RoleName.ROLE_NURSE);
        createRoleIfNotExists(RoleName.ROLE_DOCTOR);
        createRoleIfNotExists(RoleName.ROLE_RECEPTIONIST);
    }

    private void createRoleIfNotExists(RoleName roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            logger.info("تم إنشاء الدور: {}", roleName);
        } else {
            logger.info("الدور موجود بالفعل: {}", roleName);
        }
    }
}
