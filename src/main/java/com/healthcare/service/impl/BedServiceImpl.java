package com.healthcare.service.impl;

import com.healthcare.model.Bed;
import com.healthcare.repo.BedRepository;
import com.healthcare.service.interfaces.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Simple implementation of BedService
 */
@Service
public class BedServiceImpl implements BedService {
    
    @Autowired
    private BedRepository bedRepository;
    
    @Override
    public Bed save(Bed bed) {
        return bedRepository.save(bed);
    }
    
    @Override
    public Optional<Bed> findById(Long id) {
        return bedRepository.findById(id);
    }
    
    @Override
    public List<Bed> findAll() {
        return bedRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        bedRepository.deleteById(id);
    }
    
    @Override
    public List<Bed> findByWard(String wardName) {
        return bedRepository.findByWardName(wardName);
    }
    
    @Override
    public List<Bed> findByRoom(String wardName, String roomNumber) {
        return bedRepository.findByWardNameAndRoomNumber(wardName, roomNumber);
    }
    
    @Override
    public Optional<Bed> findByLocation(String wardName, String roomNumber, String bedNumber) {
        return bedRepository.findByWardNameAndRoomNumberAndBedNumber(wardName, roomNumber, bedNumber);
    }
    
    @Override
    public List<Bed> findVacantBeds() {
        return bedRepository.findByOccupiedByIsNull();
    }
    
    @Override
    public List<Bed> findOccupiedBeds() {
        return bedRepository.findByOccupiedByIsNotNull();
    }
    
    @Override
    public Optional<Bed> findByResident(Long residentId) {
        return bedRepository.findByOccupiedBy(residentId);
    }
}
