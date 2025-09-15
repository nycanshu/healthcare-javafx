package com.healthcare.repo;

import com.healthcare.model.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository for Archive entity - Basic CRUD operations only
 */
@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    
    // Find by resident
    List<Archive> findByResidentId(Long residentId);
}