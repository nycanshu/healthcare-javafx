package com.healthcare.services.impl;

import com.healthcare.model.Bed;

import java.util.List;
import java.util.Optional;

/**
 * Simple CRUD interface for Bed operations
 */
public interface IBedService {

    // Basic CRUD operations
    Bed save(Bed bed);
    Optional<Bed> findById(Long id);
    List<Bed> findAll();
    void deleteById(Long id);
    Bed update(Bed bed);
    
    // Business operations
    List<Bed> findVacantBeds();
    List<Bed> findOccupiedBeds();
    List<Bed> findByWard(String wardName);
    void assignResidentToBed(Long bedId, Long residentId);
    void vacateBed(Long bedId);
    boolean isBedAvailable(Long bedId);
}
