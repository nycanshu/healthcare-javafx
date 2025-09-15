package com.healthcare.repo;

import com.healthcare.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Simple repository for Staff entity - Basic CRUD operations only
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    // Authentication
    Optional<Staff> findByUsername(String username);
    Optional<Staff> findByUsernameAndPassword(String username, String password);
    
    // Find by role
    List<Staff> findByRole(Staff.Role role);
    
    // Check if username exists
    boolean existsByUsername(String username);
}