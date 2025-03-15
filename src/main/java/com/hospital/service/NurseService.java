package com.hospital.service;

import com.hospital.dto.NurseDTO;
import java.util.List;

public interface NurseService {
    NurseDTO createNurse(NurseDTO nurseDTO);
    NurseDTO getNurseById(Long id);
    List<NurseDTO> getAllNurses();
    NurseDTO updateNurse(Long id, NurseDTO nurseDTO);
    void deleteNurse(Long id);
}