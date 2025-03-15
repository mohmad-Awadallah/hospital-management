package com.hospital.service;

import com.hospital.dto.LabResultDTO;
import java.util.List;

public interface LabResultService {
    LabResultDTO createLabResult(LabResultDTO labResultDTO);
    LabResultDTO getLabResultById(Long id);
    List<LabResultDTO> getAllLabResults();
    LabResultDTO updateLabResult(Long id, LabResultDTO labResultDTO);
    void deleteLabResult(Long id);
}