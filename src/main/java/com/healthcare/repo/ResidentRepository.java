package com.healthcare.repo;

import com.healthcare.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Simple repository for Resident entity - Basic CRUD operations only
 */
@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    
    // Basic search operations
    List<Resident> findByFirstNameContainingIgnoreCase(String firstName);
    List<Resident> findByLastNameContainingIgnoreCase(String lastName);
    List<Resident> findByGender(Resident.Gender gender);
    
    // Find active residents
    List<Resident> findByIsActiveTrue();
    List<Resident> findByIsActiveFalse();
    
    // Find by bed
    Optional<Resident> findByCurrentBedId(Long bedId);
}