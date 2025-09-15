package com.healthcare.service.interfaces;

import com.healthcare.model.Resident;

import java.util.List;
import java.util.Optional;

/**
 * Simple service interface for Resident operations
 */
public interface ResidentService {
    
    // Basic CRUD operations
    Resident save(Resident resident);
    Optional<Resident> findById(Long id);
    List<Resident> findAll();
    void deleteById(Long id);
    
    // Basic search operations
    List<Resident> findByFirstName(String firstName);
    List<Resident> findByLastName(String lastName);
    List<Resident> findByGender(Resident.Gender gender);
    List<Resident> findActiveResidents();
    List<Resident> findInactiveResidents();
}