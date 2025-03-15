package com.hospital.service.impl;

import com.hospital.dto.PharmacyDTO;
import com.hospital.model.Pharmacy;
import com.hospital.repository.PharmacyRepository;
import com.hospital.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    @Override
    public PharmacyDTO createPharmacy(PharmacyDTO pharmacyDTO) {
        // تحويل DTO إلى Entity
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setMedicineName(pharmacyDTO.getMedicineName());
        pharmacy.setQuantity(pharmacyDTO.getQuantity());
        pharmacy.setPrice(pharmacyDTO.getPrice());
        pharmacy.setDescription(pharmacyDTO.getDescription());

        // حفظ الصيدلية في قاعدة البيانات
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        // تحويل Entity إلى DTO وإرجاعه
        return convertToDTO(savedPharmacy);
    }

    @Override
    public PharmacyDTO getPharmacyById(Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));
        return convertToDTO(pharmacy);
    }

    @Override
    public List<PharmacyDTO> getAllPharmacies() {
        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        return pharmacies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PharmacyDTO updatePharmacy(Long id, PharmacyDTO pharmacyDTO) {
        Pharmacy existingPharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacy not found"));

        // تحديث الحقول
        existingPharmacy.setMedicineName(pharmacyDTO.getMedicineName());
        existingPharmacy.setQuantity(pharmacyDTO.getQuantity());
        existingPharmacy.setPrice(pharmacyDTO.getPrice());
        existingPharmacy.setDescription(pharmacyDTO.getDescription());

        // حفظ التحديثات في قاعدة البيانات
        Pharmacy updatedPharmacy = pharmacyRepository.save(existingPharmacy);

        // تحويل Entity إلى DTO وإرجاعه
        return convertToDTO(updatedPharmacy);
    }

    @Override
    public void deletePharmacy(Long id) {
        pharmacyRepository.deleteById(id);
    }

    // طريقة مساعدة لتحويل Pharmacy إلى PharmacyDTO
    private PharmacyDTO convertToDTO(Pharmacy pharmacy) {
        PharmacyDTO dto = new PharmacyDTO();
        dto.setId(pharmacy.getId());
        dto.setMedicineName(pharmacy.getMedicineName());
        dto.setQuantity(pharmacy.getQuantity());
        dto.setPrice(pharmacy.getPrice());
        dto.setDescription(pharmacy.getDescription());
        return dto;
    }
}