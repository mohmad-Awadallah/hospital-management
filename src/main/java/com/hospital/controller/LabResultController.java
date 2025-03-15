package com.hospital.controller;

import com.hospital.dto.LabResultDTO;
import com.hospital.service.LabResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-results")
@RequiredArgsConstructor
public class LabResultController {

    private final LabResultService labResultService;

    // إنشاء نتيجة مختبر جديدة
    @PostMapping
    public ResponseEntity<LabResultDTO> createLabResult(@RequestBody LabResultDTO labResultDTO) {
        LabResultDTO createdLabResult = labResultService.createLabResult(labResultDTO);
        return new ResponseEntity<>(createdLabResult, HttpStatus.CREATED);
    }

    // الحصول على نتيجة مختبر بواسطة ID
    @GetMapping("/{id}")
    public ResponseEntity<LabResultDTO> getLabResultById(@PathVariable Long id) {
        LabResultDTO labResultDTO = labResultService.getLabResultById(id);
        return ResponseEntity.ok(labResultDTO);
    }

    // الحصول على جميع نتائج المختبر
    @GetMapping
    public ResponseEntity<List<LabResultDTO>> getAllLabResults() {
        List<LabResultDTO> labResults = labResultService.getAllLabResults();
        return ResponseEntity.ok(labResults);
    }

    // تحديث نتيجة مختبر موجودة
    @PutMapping("/{id}")
    public ResponseEntity<LabResultDTO> updateLabResult(@PathVariable Long id, @RequestBody LabResultDTO labResultDTO) {
        LabResultDTO updatedLabResult = labResultService.updateLabResult(id, labResultDTO);
        return ResponseEntity.ok(updatedLabResult);
    }

    // حذف نتيجة مختبر بواسطة ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabResult(@PathVariable Long id) {
        labResultService.deleteLabResult(id);
        return ResponseEntity.noContent().build();
    }
}
