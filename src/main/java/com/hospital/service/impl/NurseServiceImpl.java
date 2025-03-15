package com.hospital.service.impl;

import com.hospital.dto.NurseDTO;
import com.hospital.model.Nurse;
import com.hospital.model.Department;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.service.NurseService;
import com.hospital.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public NurseDTO createNurse(NurseDTO nurseDTO) {
        // تحويل DTO إلى كيان Nurse
        Nurse nurse = new Nurse();
        nurse.setName(nurseDTO.getName());
        nurse.setPhone(nurseDTO.getPhone());
        nurse.setAddress(nurseDTO.getAddress());
        nurse.setBirthDate(nurseDTO.getBirthDate());
        nurse.setGender(nurseDTO.getGender());
        nurse.setHireDate(nurseDTO.getHireDate());

        // البحث عن القسم المرتبط
        Department department = departmentRepository.findById(nurseDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + nurseDTO.getDepartmentId()));
        nurse.setDepartment(department);

        // حفظ الممرض في قاعدة البيانات
        Nurse savedNurse = nurseRepository.save(nurse);

        // تحويل الكيان المحفوظ إلى DTO وإرجاعه
        return convertToNurseDTO(savedNurse);
    }

    @Override
    @Transactional(readOnly = true)
    public NurseDTO getNurseById(Long id) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + id));
        return convertToNurseDTO(nurse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NurseDTO> getAllNurses() {
        List<Nurse> nurses = nurseRepository.findAll();
        return nurses.stream()
                .map(this::convertToNurseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NurseDTO updateNurse(Long id, NurseDTO nurseDTO) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + id));

        // تحديث الحقول
        nurse.setName(nurseDTO.getName());
        nurse.setPhone(nurseDTO.getPhone());
        nurse.setAddress(nurseDTO.getAddress());
        nurse.setBirthDate(nurseDTO.getBirthDate());
        nurse.setGender(nurseDTO.getGender());
        nurse.setHireDate(nurseDTO.getHireDate());

        // تحديث القسم إذا تم تغييره
        if (!nurse.getDepartment().getId().equals(nurseDTO.getDepartmentId())) {
            Department department = departmentRepository.findById(nurseDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + nurseDTO.getDepartmentId()));
            nurse.setDepartment(department);
        }

        // حفظ التحديثات
        Nurse updatedNurse = nurseRepository.save(nurse);
        return convertToNurseDTO(updatedNurse);
    }

    @Override
    @Transactional
    public void deleteNurse(Long id) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id: " + id));
        nurseRepository.delete(nurse);
    }

    // تحويل كيان Nurse إلى NurseDTO
    private NurseDTO convertToNurseDTO(Nurse nurse) {
        return NurseDTO.builder()
                .id(nurse.getId())
                .name(nurse.getName())
                .phone(nurse.getPhone())
                .address(nurse.getAddress())
                .birthDate(nurse.getBirthDate())
                .gender(nurse.getGender())
                .hireDate(nurse.getHireDate())
                .departmentId(nurse.getDepartment().getId())
                .build();
    }
}