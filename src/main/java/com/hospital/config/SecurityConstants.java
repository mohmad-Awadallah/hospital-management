package com.hospital.config;

import java.util.HashMap;
import java.util.Map;

public class SecurityConstants {

    // تعريف الأدوار بدون ROLE_
    public static final String ADMIN = "ADMIN"; // لا تضع ROLE_ في الأدوار
    public static final String DOCTOR = "DOCTOR";
    public static final String NURSE = "NURSE";
    public static final String RECEPTIONIST = "RECEPTIONIST";
    public static final String PHARMACIST = "PHARMACIST";
    public static final String PATIENT = "PATIENT";

    // النقاط المفتوحة (لا تتطلب مصادقة)
    public static final String[] OPEN_ENDPOINTS = {
            "/h2-console/**",
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**",
            "/favicon.ico",
            "/options/**"
    };

    // تعريف الأدوار لكل مسار
    private static final String[] ADMIN_ONLY = {ADMIN};
    private static final String[] DOCTOR_AND_NURSE = {DOCTOR, NURSE};
    private static final String[] RECEPTIONIST_ONLY = {RECEPTIONIST};
    private static final String[] PHARMACIST_ONLY = {PHARMACIST};
    private static final String[] PATIENT_ONLY = {PATIENT};
    private static final String[] DOCTOR_PATIENT_RECEPTIONIST = {DOCTOR, PATIENT, RECEPTIONIST};
    private static final String[] ADMIN_DOCTOR_RECEPTIONIST = {ADMIN, DOCTOR, RECEPTIONIST};
    private static final String[] ADMIN_RECEPTIONIST = {ADMIN, RECEPTIONIST};
    private static final String[] ADMIN_PATIENT = {ADMIN, PATIENT};
    private static final String[] ADMIN_DOCTOR_NURSE = {ADMIN, DOCTOR, NURSE};
    private static final String[] ADMIN_NURSE_RECEPTIONIST = {ADMIN, NURSE, RECEPTIONIST};
    private static final String[] ADMIN_DOCTOR_PHARMACIST_NURSE = {ADMIN, DOCTOR, PHARMACIST, NURSE};

    // دمج جميع المسارات في خريطة واحدة
    public static final Map<String, String[]> RESOURCE_ROLES = new HashMap<>() {{
        // مسارات خاصة بمسؤولي النظام
        put("/api/roles/**", ADMIN_ONLY);
        put("/api/admin/**", ADMIN_ONLY);
        put("/api/users/**", ADMIN_ONLY);

        // مسارات خاصة بالطاقم الطبي
        put("/api/lab-results/**", ADMIN_DOCTOR_NURSE);

        // مسارات خاصة بموظفي الاستقبال
        put("/api/reception/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالصيادلة
        put("/api/pharmacy/**", PHARMACIST_ONLY);

        // مسارات خاصة بالمرضى
        put("/api/patients/**", ADMIN_PATIENT);

        // مسارات خاصة بالمواعيد
        put("/api/appointments/**", DOCTOR_PATIENT_RECEPTIONIST);

        // مسارات خاصة بالأطباء
        put("/api/doctors/**", ADMIN_DOCTOR_RECEPTIONIST);

        // مسارات خاصة بالأقسام
        put("/api/departments/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالسجلات الطبية
        put("/api/medical-records/**", ADMIN_DOCTOR_NURSE);

        // مسارات خاصة بالقبول
        put("/api/admissions/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالغرف
        put("/api/rooms/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالفواتير
        put("/api/bills/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالممرضين
        put("/api/nurses/**", ADMIN_NURSE_RECEPTIONIST);

        // مسارات خاصة بالدفعات
        put("/api/payments/**", ADMIN_RECEPTIONIST);

        // مسارات خاصة بالوصفات الطبية
        put("/api/prescriptions/**", ADMIN_DOCTOR_PHARMACIST_NURSE);
    }};
}