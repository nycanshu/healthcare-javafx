package com.healthcare.repo;

import com.healthcare.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Simple repository for Medicine entity - Basic CRUD operations only
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    // Find by name
    Optional<Medicine> findByName(String name);
    List<Medicine> findByNameContainingIgnoreCase(String name);
    
    // Check if exists
    boolean existsByName(String name);
}