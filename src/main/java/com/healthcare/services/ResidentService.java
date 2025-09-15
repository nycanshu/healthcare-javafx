package com.healthcare.services;

import com.healthcare.config.DBConnection;
import com.healthcare.model.Resident;
import com.healthcare.services.impl.IResidentService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple ResidentService implementation using MySQL CRUD operations
 */
public class ResidentService implements IResidentService {

    @Override
    public Resident save(Resident resident) {
        String sql = "INSERT INTO Residents (first_name, last_name, gender, birth_date, admission_date, discharge_date, current_bed_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, resident.getFirstName());
            stmt.setString(2, resident.getLastName());
            stmt.setString(3, resident.getGender().name());
            stmt.setDate(4, resident.getBirthDate() != null ? Date.valueOf(resident.getBirthDate()) : null);
            stmt.setDate(5, Date.valueOf(resident.getAdmissionDate()));
            stmt.setDate(6, resident.getDischargeDate() != null ? Date.valueOf(resident.getDischargeDate()) : null);
            stmt.setLong(7, resident.getCurrentBedId() != null ? resident.getCurrentBedId() : 0);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    resident.setResidentId(generatedKeys.getLong(1));
                }
            }
            return resident;
            
        } catch (SQLException e) {
            System.err.println("Error saving resident: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Resident> findById(Long id) {
        String sql = "SELECT * FROM Residents WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToResident(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding resident by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public List<Resident> findAll() {
        String sql = "SELECT * FROM Residents ORDER BY admission_date DESC";
        List<Resident> residentList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                residentList.add(mapResultSetToResident(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all residents: " + e.getMessage());
        }
        
        return residentList;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Residents WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting resident: " + e.getMessage());
        }
    }

    @Override
    public Resident update(Resident resident) {
        String sql = "UPDATE Residents SET first_name = ?, last_name = ?, gender = ?, birth_date = ?, admission_date = ?, discharge_date = ?, current_bed_id = ? WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, resident.getFirstName());
            stmt.setString(2, resident.getLastName());
            stmt.setString(3, resident.getGender().name());
            stmt.setDate(4, resident.getBirthDate() != null ? Date.valueOf(resident.getBirthDate()) : null);
            stmt.setDate(5, Date.valueOf(resident.getAdmissionDate()));
            stmt.setDate(6, resident.getDischargeDate() != null ? Date.valueOf(resident.getDischargeDate()) : null);
            stmt.setLong(7, resident.getCurrentBedId() != null ? resident.getCurrentBedId() : 0);
            stmt.setLong(8, resident.getResidentId());
            
            stmt.executeUpdate();
            return resident;
            
        } catch (SQLException e) {
            System.err.println("Error updating resident: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Resident> findActiveResidents() {
        String sql = "SELECT * FROM Residents WHERE discharge_date IS NULL ORDER BY admission_date DESC";
        List<Resident> residentList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                residentList.add(mapResultSetToResident(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding active residents: " + e.getMessage());
        }
        
        return residentList;
    }

    @Override
    public List<Resident> findDischargedResidents() {
        String sql = "SELECT * FROM Residents WHERE discharge_date IS NOT NULL ORDER BY discharge_date DESC";
        List<Resident> residentList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                residentList.add(mapResultSetToResident(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding discharged residents: " + e.getMessage());
        }
        
        return residentList;
    }

    @Override
    public Resident admitResident(Resident resident, Long bedId) {
        // First save the resident
        Resident savedResident = save(resident);
        if (savedResident != null && bedId != null) {
            // Assign bed
            assignBed(savedResident.getResidentId(), bedId);
        }
        return savedResident;
    }

    @Override
    public void dischargeResident(Long residentId) {
        String sql = "UPDATE Residents SET discharge_date = ?, current_bed_id = NULL WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setLong(2, residentId);
            stmt.executeUpdate();
            
            // Also update the bed to be vacant
            unassignBed(residentId);
            
        } catch (SQLException e) {
            System.err.println("Error discharging resident: " + e.getMessage());
        }
    }

    @Override
    public void assignBed(Long residentId, Long bedId) {
        String sql = "UPDATE Residents SET current_bed_id = ? WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bedId);
            stmt.setLong(2, residentId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error assigning bed: " + e.getMessage());
        }
    }

    @Override
    public void unassignBed(Long residentId) {
        String sql = "UPDATE Residents SET current_bed_id = NULL WHERE resident_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, residentId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error unassigning bed: " + e.getMessage());
        }
    }

    private Resident mapResultSetToResident(ResultSet rs) throws SQLException {
        Resident resident = new Resident();
        resident.setResidentId(rs.getLong("resident_id"));
        resident.setFirstName(rs.getString("first_name"));
        resident.setLastName(rs.getString("last_name"));
        resident.setGender(Resident.Gender.valueOf(rs.getString("gender")));
        
        Date birthDate = rs.getDate("birth_date");
        resident.setBirthDate(birthDate != null ? birthDate.toLocalDate() : null);
        
        resident.setAdmissionDate(rs.getDate("admission_date").toLocalDate());
        
        Date dischargeDate = rs.getDate("discharge_date");
        resident.setDischargeDate(dischargeDate != null ? dischargeDate.toLocalDate() : null);
        
        long currentBedId = rs.getLong("current_bed_id");
        resident.setCurrentBedId(currentBedId > 0 ? currentBedId : null);
        
        return resident;
    }
}
