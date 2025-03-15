package com.hospital.config;

import com.hospital.security.CustomAccessDeniedHandler;
import com.hospital.security.JwtAuthenticationExceptionHandler;
import com.hospital.security.JwtAuthenticationFilter;
import com.hospital.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil,
                          @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler,
                          CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationExceptionHandler = jwtAuthenticationExceptionHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("تهيئة سلسلة فلاتر الأمان");
        http
                .cors(cors -> cors.configure(http)) // دعم CORS
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionFixation().migrateSession())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(SecurityConstants.OPEN_ENDPOINTS).permitAll(); // السماح بالوصول إلى Swagger
                    SecurityConstants.RESOURCE_ROLES.forEach((path, roles) -> {
                        auth.requestMatchers(path).hasAnyRole(roles);
                    });
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(this::handleAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .preload(true)
                                .maxAgeInSeconds(63072000)));
        return http.build();
    }

    private void handleAuthenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, Exception authException) throws IOException {
        logger.error("خطأ في المصادقة", authException);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"error\": \"Unauthorized access\", \"message\": \"" + authException.getMessage() + "\"}");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}