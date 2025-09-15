package com.healthcare.service.impl;

import com.healthcare.model.Prescription;
import com.healthcare.repo.PrescriptionRepository;
import com.healthcare.service.interfaces.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of PrescriptionService
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Override
    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }
    
    @Override
    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }
    
    @Override
    public List<Prescription> findAll() {
        return prescriptionRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        prescriptionRepository.deleteById(id);
    }
    
    @Override
    public List<Prescription> findByResident(Long residentId) {
        return prescriptionRepository.findByResidentId(residentId);
    }
    
    @Override
    public List<Prescription> findByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}
