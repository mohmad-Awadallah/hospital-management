package com.hospital.repository;

import com.hospital.model.Appointment;
import com.hospital.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // البحث عن المواعيد عن طريق المريض
    List<Appointment> findByPatientId(Long patientId);

    // البحث عن المواعيد عن طريق الطبيب
    List<Appointment> findByDoctorId(Long doctorId);

    // البحث عن المواعيد بين تاريخين
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // البحث عن المواعيد بحالة معينة (مثال: SCHEDULED, COMPLETED)
    List<Appointment> findByStatus(AppointmentStatus status);
}