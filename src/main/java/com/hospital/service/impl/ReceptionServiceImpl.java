package com.hospital.service.impl;

import com.hospital.dto.ReceptionDTO;
import com.hospital.model.Reception;
import com.hospital.model.Patient;
import com.hospital.model.Doctor;
import com.hospital.repository.ReceptionRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.service.ReceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionServiceImpl implements ReceptionService {

    private final ReceptionRepository receptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public ReceptionDTO createReception(ReceptionDTO receptionDTO) {
        // تحويل DTO إلى Entity يدويًا
        Reception reception = new Reception();
        reception.setReceptionDate(receptionDTO.getReceptionDate());
        reception.setNotes(receptionDTO.getNotes());

        // جلب المريض والطبيب من قاعدة البيانات
        Patient patient = patientRepository.findById(receptionDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(receptionDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // تعيين المريض والطبيب للاستقبال
        reception.setPatient(patient);
        reception.setDoctor(doctor);

        // حفظ الاستقبال في قاعدة البيانات
        Reception savedReception = receptionRepository.save(reception);

        // تحويل Entity إلى DTO يدويًا وإرجاعه
        return convertToDTO(savedReception);
    }

    @Override
    public ReceptionDTO getReceptionById(Long id) {
        Reception reception = receptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reception not found"));
        return convertToDTO(reception);
    }

    @Override
    public List<ReceptionDTO> getAllReceptions() {
        List<Reception> receptions = receptionRepository.findAll();
        return receptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReceptionDTO updateReception(Long id, ReceptionDTO receptionDTO) {
        Reception existingReception = receptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reception not found"));

        // تحديث الحقول الأساسية يدويًا
        existingReception.setReceptionDate(receptionDTO.getReceptionDate());
        existingReception.setNotes(receptionDTO.getNotes());

        // تحديث المريض والطبيب إذا تم تغييرهم
        if (receptionDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(receptionDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            existingReception.setPatient(patient);
        }
        if (receptionDTO.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(receptionDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            existingReception.setDoctor(doctor);
        }

        // حفظ التحديثات في قاعدة البيانات
        Reception updatedReception = receptionRepository.save(existingReception);

        // تحويل Entity إلى DTO يدويًا وإرجاعه
        return convertToDTO(updatedReception);
    }

    @Override
    public void deleteReception(Long id) {
        receptionRepository.deleteById(id);
    }

    // طريقة مساعدة لتحويل Reception إلى ReceptionDTO
    private ReceptionDTO convertToDTO(Reception reception) {
        ReceptionDTO dto = new ReceptionDTO();
        dto.setId(reception.getId());
        dto.setReceptionDate(reception.getReceptionDate());
        dto.setNotes(reception.getNotes());

        // إضافة ID واسم المريض
        if (reception.getPatient() != null) {
            dto.setPatientId(reception.getPatient().getId());
            dto.setPatientName(reception.getPatient().getName()); // اسم المريض
        }

        // إضافة ID واسم الطبيب
        if (reception.getDoctor() != null) {
            dto.setDoctorId(reception.getDoctor().getId());
            dto.setDoctorName(reception.getDoctor().getName()); // اسم الطبيب
        }

        return dto;
    }
}