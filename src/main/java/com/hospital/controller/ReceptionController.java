package com.hospital.controller;

import com.hospital.dto.ReceptionDTO;
import com.hospital.service.ReceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
public class ReceptionController {

    private final ReceptionService receptionService;

    @PostMapping
    public ResponseEntity<ReceptionDTO> createReception(@RequestBody ReceptionDTO receptionDTO) {
        ReceptionDTO createdReception = receptionService.createReception(receptionDTO);
        return new ResponseEntity<>(createdReception, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionDTO> getReceptionById(@PathVariable Long id) {
        ReceptionDTO receptionDTO = receptionService.getReceptionById(id);
        return ResponseEntity.ok(receptionDTO);
    }

    @GetMapping
    public ResponseEntity<List<ReceptionDTO>> getAllReceptions() {
        List<ReceptionDTO> receptions = receptionService.getAllReceptions();
        return ResponseEntity.ok(receptions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceptionDTO> updateReception(@PathVariable Long id, @RequestBody ReceptionDTO receptionDTO) {
        ReceptionDTO updatedReception = receptionService.updateReception(id, receptionDTO);
        return ResponseEntity.ok(updatedReception);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReception(@PathVariable Long id) {
        receptionService.deleteReception(id);
        return ResponseEntity.noContent().build();
    }
}
