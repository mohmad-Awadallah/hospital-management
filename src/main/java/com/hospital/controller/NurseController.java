package com.hospital.controller;

import com.hospital.dto.NurseDTO;
import com.hospital.service.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
public class NurseController {

    private final NurseService nurseService;

    @PostMapping
    public ResponseEntity<NurseDTO> createNurse(@RequestBody NurseDTO nurseDTO) {
        NurseDTO createdNurse = nurseService.createNurse(nurseDTO);
        return new ResponseEntity<>(createdNurse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NurseDTO> getNurseById(@PathVariable Long id) {
        NurseDTO nurseDTO = nurseService.getNurseById(id);
        return ResponseEntity.ok(nurseDTO);
    }

    @GetMapping
    public ResponseEntity<List<NurseDTO>> getAllNurses() {
        List<NurseDTO> nurses = nurseService.getAllNurses();
        return ResponseEntity.ok(nurses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NurseDTO> updateNurse(@PathVariable Long id, @RequestBody NurseDTO nurseDTO) {
        NurseDTO updatedNurse = nurseService.updateNurse(id, nurseDTO);
        return ResponseEntity.ok(updatedNurse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        nurseService.deleteNurse(id);
        return ResponseEntity.noContent().build();
    }
}