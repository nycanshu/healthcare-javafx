package com.healthcare.service.interfaces;

import com.healthcare.model.Staff;

import java.util.List;
import java.util.Optional;

/**
 * Simple service interface for Staff operations
 */
public interface StaffService {
    
    // Basic CRUD operations
    Staff save(Staff staff);
    Optional<Staff> findById(Long id);
    List<Staff> findAll();
    void deleteById(Long id);
    
    // Authentication
    Optional<Staff> authenticate(String username, String password);
    boolean isUsernameAvailable(String username);
    
    // Find by role
    List<Staff> findByRole(Staff.Role role);
    List<Staff> findAllDoctors();
    List<Staff> findAllNurses();
    List<Staff> findAllManagers();
}