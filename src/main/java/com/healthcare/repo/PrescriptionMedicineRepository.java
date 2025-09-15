package com.healthcare.repo;

import com.healthcare.model.PrescriptionMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository for PrescriptionMedicine entity - Basic CRUD operations only
 */
@Repository
public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, Long> {
    
    // Find by prescription
    List<PrescriptionMedicine> findByPrescriptionId(Long prescriptionId);
    
    // Find by medicine
    List<PrescriptionMedicine> findByMedicineId(Long medicineId);
}