package com.hospital.controller;

import com.hospital.dto.AdmissionDTO;
import com.hospital.model.Admission;
import com.hospital.service.AdmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private static final Logger logger = LoggerFactory.getLogger(AdmissionController.class);

    @Autowired
    private AdmissionService admissionService;

    @PostMapping
    public ResponseEntity<AdmissionDTO> createAdmission(@Valid @RequestBody Admission admission) {
        logger.debug("طلب إنشاء قبول جديد: {}", admission);
        Admission createdAdmission = admissionService.createAdmission(admission);
        AdmissionDTO admissionDTO = admissionService.convertToAdmissionDTO(createdAdmission);
        logger.info("تم إنشاء قبول جديد بنجاح: {}", createdAdmission);
        return new ResponseEntity<>(admissionDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdmissionDTO>> getAllAdmissions() {
        logger.debug("جلب جميع القبولات");
        List<Admission> admissions = admissionService.getAllAdmissions();
        List<AdmissionDTO> admissionDTOs = admissions.stream()
                .map(admissionService::convertToAdmissionDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(admissionDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmissionDTO> getAdmissionById(@PathVariable Long id) {
        logger.debug("جلب القبول بواسطة ID: {}", id);
        Admission admission = admissionService.getAdmissionById(id);
        AdmissionDTO admissionDTO = admissionService.convertToAdmissionDTO(admission);
        return new ResponseEntity<>(admissionDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdmissionDTO> updateAdmission(@PathVariable Long id, @Valid @RequestBody Admission admission) {
        logger.debug("تحديث القبول: {}", admission);
        Admission updatedAdmission = admissionService.updateAdmission(id, admission);
        AdmissionDTO admissionDTO = admissionService.convertToAdmissionDTO(updatedAdmission);
        logger.info("تم تحديث القبول بنجاح: {}", updatedAdmission);
        return new ResponseEntity<>(admissionDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmission(@PathVariable Long id) {
        logger.debug("حذف القبول بواسطة ID: {}", id);
        admissionService.deleteAdmission(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/appointment")
    public ResponseEntity<AdmissionDTO> getAdmissionAsAppointment(@PathVariable Long id) {
        logger.debug("جلب القبول كموعد بواسطة ID: {}", id);
        AdmissionDTO admissionDTO = admissionService.convertToAdmissionDTO(id);
        return new ResponseEntity<>(admissionDTO, HttpStatus.OK);
    }
}