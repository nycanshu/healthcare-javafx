package com.healthcare.service.impl;

import com.healthcare.model.Resident;
import com.healthcare.repo.ResidentRepository;
import com.healthcare.service.interfaces.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of ResidentService
 */
@Service
public class ResidentServiceImpl implements ResidentService {
    
    @Autowired
    private ResidentRepository residentRepository;
    
    @Override
    public Resident save(Resident resident) {
        return residentRepository.save(resident);
    }
    
    @Override
    public Optional<Resident> findById(Long id) {
        return residentRepository.findById(id);
    }
    
    @Override
    public List<Resident> findAll() {
        return residentRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        residentRepository.deleteById(id);
    }
    
    @Override
    public List<Resident> findByFirstName(String firstName) {
        return residentRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
    
    @Override
    public List<Resident> findByLastName(String lastName) {
        return residentRepository.findByLastNameContainingIgnoreCase(lastName);
    }
    
    @Override
    public List<Resident> findByGender(Resident.Gender gender) {
        return residentRepository.findByGender(gender);
    }
    
    @Override
    public List<Resident> findActiveResidents() {
        return residentRepository.findByIsActiveTrue();
    }
    
    @Override
    public List<Resident> findInactiveResidents() {
        return residentRepository.findByIsActiveFalse();
    }
}
