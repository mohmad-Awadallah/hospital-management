package com.hospital.service;

import com.hospital.dto.ReceptionDTO;
import java.util.List;

public interface ReceptionService {
    ReceptionDTO createReception(ReceptionDTO receptionDTO);
    ReceptionDTO getReceptionById(Long id);
    List<ReceptionDTO> getAllReceptions();
    ReceptionDTO updateReception(Long id, ReceptionDTO receptionDTO);
    void deleteReception(Long id);
}