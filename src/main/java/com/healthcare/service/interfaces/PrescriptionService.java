package com.healthcare.service.interfaces;

import com.healthcare.model.Prescription;

import java.util.List;
import java.util.Optional;

/**
 * Simple service interface for Prescription operations
 */
public interface PrescriptionService {
    
    // Basic CRUD operations
    Prescription save(Prescription prescription);
    Optional<Prescription> findById(Long id);
    List<Prescription> findAll();
    void deleteById(Long id);
    
    // Find by resident or doctor
    List<Prescription> findByResident(Long residentId);
    List<Prescription> findByDoctor(Long doctorId);
}