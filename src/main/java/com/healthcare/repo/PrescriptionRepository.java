package com.healthcare.repo;

import com.healthcare.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository for Prescription entity - Basic CRUD operations only
 */
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    // Find by resident
    List<Prescription> findByResidentId(Long residentId);
    
    // Find by doctor
    List<Prescription> findByDoctorId(Long doctorId);
}