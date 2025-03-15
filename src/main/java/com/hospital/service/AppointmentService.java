package com.hospital.service;

import com.hospital.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment addAppointment(Appointment appointment);
    Optional<Appointment> getAppointment(Long id);
    List<Appointment> getAllAppointments();
    Optional<Appointment> updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);
    boolean existsById(Long id);
}
