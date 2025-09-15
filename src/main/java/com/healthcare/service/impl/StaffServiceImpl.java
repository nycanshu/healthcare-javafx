package com.healthcare.service.impl;

import com.healthcare.model.Staff;
import com.healthcare.repo.StaffRepository;
import com.healthcare.service.interfaces.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of StaffService
 */
@Service
public class StaffServiceImpl implements StaffService {
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }
    
    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }
    
    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }
    
    @Override
    public Optional<Staff> authenticate(String username, String password) {
        return staffRepository.findByUsernameAndPassword(username, password);
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        return !staffRepository.existsByUsername(username);
    }
    
    @Override
    public List<Staff> findByRole(Staff.Role role) {
        return staffRepository.findByRole(role);
    }
    
    @Override
    public List<Staff> findAllDoctors() {
        return staffRepository.findByRole(Staff.Role.Doctor);
    }
    
    @Override
    public List<Staff> findAllNurses() {
        return staffRepository.findByRole(Staff.Role.Nurse);
    }
    
    @Override
    public List<Staff> findAllManagers() {
        return staffRepository.findByRole(Staff.Role.Manager);
    }
}
