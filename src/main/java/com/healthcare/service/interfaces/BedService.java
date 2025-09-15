package com.healthcare.service.interfaces;

import com.healthcare.model.Bed;

import java.util.List;
import java.util.Optional;

/**
 * Simple service interface for Bed operations
 */
public interface BedService {
    
    // Basic CRUD operations
    Bed save(Bed bed);
    Optional<Bed> findById(Long id);
    List<Bed> findAll();
    void deleteById(Long id);
    
    // Find by location
    List<Bed> findByWard(String wardName);
    List<Bed> findByRoom(String wardName, String roomNumber);
    Optional<Bed> findByLocation(String wardName, String roomNumber, String bedNumber);
    
    // Find occupied/vacant beds
    List<Bed> findVacantBeds();
    List<Bed> findOccupiedBeds();
    Optional<Bed> findByResident(Long residentId);
}