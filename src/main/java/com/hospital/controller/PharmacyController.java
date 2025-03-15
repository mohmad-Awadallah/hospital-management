package com.hospital.controller;

import com.hospital.dto.PharmacyDTO;
import com.hospital.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping
    public ResponseEntity<PharmacyDTO> createPharmacy(@RequestBody PharmacyDTO pharmacyDTO) {
        PharmacyDTO createdPharmacy = pharmacyService.createPharmacy(pharmacyDTO);
        return new ResponseEntity<>(createdPharmacy, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyDTO> getPharmacyById(@PathVariable Long id) {
        PharmacyDTO pharmacyDTO = pharmacyService.getPharmacyById(id);
        return ResponseEntity.ok(pharmacyDTO);
    }

    @GetMapping
    public ResponseEntity<List<PharmacyDTO>> getAllPharmacies() {
        List<PharmacyDTO> pharmacies = pharmacyService.getAllPharmacies();
        return ResponseEntity.ok(pharmacies);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacyDTO> updatePharmacy(@PathVariable Long id, @RequestBody PharmacyDTO pharmacyDTO) {
        PharmacyDTO updatedPharmacy = pharmacyService.updatePharmacy(id, pharmacyDTO);
        return ResponseEntity.ok(updatedPharmacy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }
}