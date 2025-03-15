package com.hospital.service;

import com.hospital.dto.PharmacyDTO;
import java.util.List;

public interface PharmacyService {
    PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO);
    PharmacyDTO getPharmacyById(Long id);
    List<PharmacyDTO> getAllPharmacies();
    PharmacyDTO updatePharmacy(Long id, PharmacyDTO pharmacyDTO);
    void deletePharmacy(Long id);
}