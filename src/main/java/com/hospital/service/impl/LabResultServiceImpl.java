package com.hospital.service.impl;

import com.hospital.dto.LabResultDTO;
import com.hospital.model.LabResult;
import com.hospital.model.Patient;
import com.hospital.model.Doctor;
import com.hospital.repository.LabResultRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.service.LabResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabResultServiceImpl implements LabResultService {

    private final LabResultRepository labResultRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public LabResultDTO createLabResult(LabResultDTO labResultDTO) {
        // تحويل DTO إلى Entity يدويًا
        LabResult labResult = new LabResult();
        labResult.setTestName(labResultDTO.getTestName());
        labResult.setResult(labResultDTO.getResult());
        labResult.setDate(labResultDTO.getDate());
        labResult.setStatus(labResultDTO.getStatus());
        labResult.setNotes(labResultDTO.getNotes());

        // جلب المريض والطبيب من قاعدة البيانات
        Patient patient = patientRepository.findById(labResultDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(labResultDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // تعيين المريض والطبيب لنتيجة المختبر
        labResult.setPatient(patient);
        labResult.setDoctor(doctor);

        // حفظ نتيجة المختبر في قاعدة البيانات
        LabResult savedLabResult = labResultRepository.save(labResult);

        // تحويل Entity إلى DTO يدويًا وإرجاعه
        return convertToDTO(savedLabResult);
    }

    @Override
    public LabResultDTO getLabResultById(Long id) {
        LabResult labResult = labResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LabResult not found"));
        return convertToDTO(labResult);
    }

    @Override
    public List<LabResultDTO> getAllLabResults() {
        List<LabResult> labResults = labResultRepository.findAll();
        return labResults.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LabResultDTO updateLabResult(Long id, LabResultDTO labResultDTO) {
        LabResult existingLabResult = labResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LabResult not found"));

        // تحديث الحقول الأساسية يدويًا
        existingLabResult.setTestName(labResultDTO.getTestName());
        existingLabResult.setResult(labResultDTO.getResult());
        existingLabResult.setDate(labResultDTO.getDate());
        existingLabResult.setStatus(labResultDTO.getStatus());
        existingLabResult.setNotes(labResultDTO.getNotes());

        // تحديث المريض والطبيب إذا تم تغييرهم
        if (labResultDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(labResultDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            existingLabResult.setPatient(patient);
        }
        if (labResultDTO.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(labResultDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            existingLabResult.setDoctor(doctor);
        }

        // حفظ التحديثات في قاعدة البيانات
        LabResult updatedLabResult = labResultRepository.save(existingLabResult);

        // تحويل Entity إلى DTO يدويًا وإرجاعه
        return convertToDTO(updatedLabResult);
    }

    @Override
    public void deleteLabResult(Long id) {
        labResultRepository.deleteById(id);
    }

    // طريقة مساعدة لتحويل LabResult إلى LabResultDTO
    private LabResultDTO convertToDTO(LabResult labResult) {
        LabResultDTO dto = new LabResultDTO();
        dto.setId(labResult.getId());
        dto.setTestName(labResult.getTestName());
        dto.setResult(labResult.getResult());
        dto.setDate(labResult.getDate());
        dto.setStatus(labResult.getStatus());
        dto.setNotes(labResult.getNotes());

        // إضافة ID واسم المريض
        if (labResult.getPatient() != null) {
            dto.setPatientId(labResult.getPatient().getId());
            dto.setPatientName(labResult.getPatient().getName()); // اسم المريض
        }

        // إضافة ID واسم الطبيب
        if (labResult.getDoctor() != null) {
            dto.setDoctorId(labResult.getDoctor().getId());
            dto.setDoctorName(labResult.getDoctor().getName()); // اسم الطبيب
        }

        return dto;
    }
}