package com.healthcare.service.interfaces;

import com.healthcare.model.AdministeredMedication;
import com.healthcare.model.Medicine;
import com.healthcare.model.PrescriptionMedicine;

import java.util.List;
import java.util.Optional;

/**
 * Simple service interface for Medication operations
 */
public interface MedicationService {
    
    // Medicine operations
    Medicine saveMedicine(Medicine medicine);
    Optional<Medicine> findMedicineById(Long id);
    List<Medicine> findAllMedicines();
    void deleteMedicineById(Long id);
    Optional<Medicine> findMedicineByName(String name);
    
    // Prescription medicine operations
    PrescriptionMedicine savePrescriptionMedicine(PrescriptionMedicine prescriptionMedicine);
    Optional<PrescriptionMedicine> findPrescriptionMedicineById(Long id);
    List<PrescriptionMedicine> findAllPrescriptionMedicines();
    void deletePrescriptionMedicineById(Long id);
    
    // Administered medication operations
    AdministeredMedication saveAdministeredMedication(AdministeredMedication administeredMedication);
    Optional<AdministeredMedication> findAdministeredMedicationById(Long id);
    List<AdministeredMedication> findAllAdministeredMedications();
    void deleteAdministeredMedicationById(Long id);
}