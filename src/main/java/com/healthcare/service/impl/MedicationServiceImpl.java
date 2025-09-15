package com.healthcare.service.impl;

import com.healthcare.model.AdministeredMedication;
import com.healthcare.model.Medicine;
import com.healthcare.model.PrescriptionMedicine;
import com.healthcare.repo.AdministeredMedicationRepository;
import com.healthcare.repo.MedicineRepository;
import com.healthcare.repo.PrescriptionMedicineRepository;
import com.healthcare.service.interfaces.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of MedicationService
 */
@Service
public class MedicationServiceImpl implements MedicationService {
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private PrescriptionMedicineRepository prescriptionMedicineRepository;
    
    @Autowired
    private AdministeredMedicationRepository administeredMedicationRepository;
    
    // Medicine operations
    @Override
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
    
    @Override
    public Optional<Medicine> findMedicineById(Long id) {
        return medicineRepository.findById(id);
    }
    
    @Override
    public List<Medicine> findAllMedicines() {
        return medicineRepository.findAll();
    }
    
    @Override
    public void deleteMedicineById(Long id) {
        medicineRepository.deleteById(id);
    }
    
    @Override
    public Optional<Medicine> findMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }
    
    // Prescription medicine operations
    @Override
    public PrescriptionMedicine savePrescriptionMedicine(PrescriptionMedicine prescriptionMedicine) {
        return prescriptionMedicineRepository.save(prescriptionMedicine);
    }
    
    @Override
    public Optional<PrescriptionMedicine> findPrescriptionMedicineById(Long id) {
        return prescriptionMedicineRepository.findById(id);
    }
    
    @Override
    public List<PrescriptionMedicine> findAllPrescriptionMedicines() {
        return prescriptionMedicineRepository.findAll();
    }
    
    @Override
    public void deletePrescriptionMedicineById(Long id) {
        prescriptionMedicineRepository.deleteById(id);
    }
    
    // Administered medication operations
    @Override
    public AdministeredMedication saveAdministeredMedication(AdministeredMedication administeredMedication) {
        return administeredMedicationRepository.save(administeredMedication);
    }
    
    @Override
    public Optional<AdministeredMedication> findAdministeredMedicationById(Long id) {
        return administeredMedicationRepository.findById(id);
    }
    
    @Override
    public List<AdministeredMedication> findAllAdministeredMedications() {
        return administeredMedicationRepository.findAll();
    }
    
    @Override
    public void deleteAdministeredMedicationById(Long id) {
        administeredMedicationRepository.deleteById(id);
    }
}
