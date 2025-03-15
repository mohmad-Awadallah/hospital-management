package com.hospital.service;
import com.hospital.model.Role;
import com.hospital.model.RoleName;
import com.hospital.model.User;
import com.hospital.repository.RoleRepository;
import com.hospital.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserInitializationService {
    private static final Logger logger = LoggerFactory.getLogger(UserInitializationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void initializeAdminUser() {
        logger.info("Initializing admin user...");

        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("AdminPass123!"));

            // Assign admin role
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> {
                        logger.error("Error: Role not found - {}", RoleName.ROLE_ADMIN);
                        return new RuntimeException("Error: Role is not found.");
                    });

            logger.info("Role found: {}", adminRole);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            admin.setRoles(roles);
            userRepository.save(admin);

            logger.info("Admin user created successfully.");
        } else {
            logger.info("Admin user already exists.");
        }
    }

    @Transactional
    public void initializeRoles() {
        logger.info("Initializing roles...");

        for (RoleName roleName : RoleName.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                logger.info("Role created: {}", roleName);
            } else {
                logger.info("Role already exists: {}", roleName);
            }
        }
    }
}