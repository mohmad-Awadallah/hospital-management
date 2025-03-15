package com.hospital.service;

import com.hospital.dto.BillDTO;

import java.util.List;

public interface BillService {
    BillDTO createBill(BillDTO billDTO);
    BillDTO getBillById(Long id);
    List<BillDTO> getAllBills();
    BillDTO updateBill(Long id, BillDTO billDTO);
    void deleteBill(Long id);
}