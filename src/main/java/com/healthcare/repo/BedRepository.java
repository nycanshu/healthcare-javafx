package com.healthcare.repo;

import com.healthcare.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Simple repository for Bed entity - Basic CRUD operations only
 */
@Repository
public interface BedRepository extends JpaRepository<Bed, Long> {
    
    // Find by location
    List<Bed> findByWardName(String wardName);
    List<Bed> findByWardNameAndRoomNumber(String wardName, String roomNumber);
    Optional<Bed> findByWardNameAndRoomNumberAndBedNumber(String wardName, String roomNumber, String bedNumber);
    
    // Find occupied/vacant beds
    List<Bed> findByOccupiedByIsNull(); // Vacant beds
    List<Bed> findByOccupiedByIsNotNull(); // Occupied beds
    
    // Find by resident
    Optional<Bed> findByOccupiedBy(Long residentId);
}