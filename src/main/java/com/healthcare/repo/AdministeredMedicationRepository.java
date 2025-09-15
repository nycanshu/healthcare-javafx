package com.healthcare.repo;

import com.healthcare.model.AdministeredMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository for AdministeredMedication entity - Basic CRUD operations only
 */
@Repository
public interface AdministeredMedicationRepository extends JpaRepository<AdministeredMedication, Long> {
    
    // Find by nurse
    List<AdministeredMedication> findByNurseId(Long nurseId);
    
    // Find by prescription medicine
    List<AdministeredMedication> findByPrescriptionMedicineId(Long prescriptionMedicineId);
}