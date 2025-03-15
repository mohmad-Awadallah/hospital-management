package com.hospital.config;

import com.hospital.service.UserInitializationService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner init(UserInitializationService userInitializationService) {
        return args -> userInitializationService.initializeAdminUser();
    }
}
