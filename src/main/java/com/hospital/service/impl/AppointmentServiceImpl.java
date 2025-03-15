package com.hospital.service.impl;

import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Appointment;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public Appointment addAppointment(Appointment appointment) {
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        Long patientId = appointment.getPatient().getId();
        Long doctorId = appointment.getDoctor().getId();

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        log.info("Adding new appointment for patient: {}", patient.getName());
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointment(Long id) {
        log.info("Fetching appointment with ID: {}", id);
        return appointmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Appointment> updateAppointment(Long id, Appointment appointment) {
        log.info("Updating appointment with ID: {}", id);

        return appointmentRepository.findById(id)
                .map(existingAppointment -> {
                    if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
                        throw new IllegalArgumentException("Patient ID cannot be null");
                    }
                    if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
                        throw new IllegalArgumentException("Doctor ID cannot be null");
                    }

                    Long patientId = appointment.getPatient().getId();
                    Long doctorId = appointment.getDoctor().getId();

                    Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
                    Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

                    existingAppointment.setAppointmentDateTime(appointment.getAppointmentDateTime());
                    existingAppointment.setStatus(appointment.getStatus());
                    existingAppointment.setPatient(patient);
                    existingAppointment.setDoctor(doctor);
                    return appointmentRepository.save(existingAppointment);
                });
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with ID: {}", id);
        appointmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return appointmentRepository.existsById(id);
    }
}