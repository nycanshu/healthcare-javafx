package com.healthcare.services;

import com.healthcare.config.DBConnection;
import com.healthcare.model.Bed;
import com.healthcare.services.impl.IBedService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple BedService implementation using MySQL CRUD operations
 */
public class BedService implements IBedService {

    @Override
    public Bed save(Bed bed) {
        String sql = "INSERT INTO Beds (ward_name, room_number, bed_number, occupied_by) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, bed.getWardName());
            stmt.setString(2, bed.getRoomNumber());
            stmt.setString(3, bed.getBedNumber());
            stmt.setLong(4, bed.getOccupiedBy() != null ? bed.getOccupiedBy() : 0);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    bed.setBedId(generatedKeys.getLong(1));
                }
            }
            return bed;
            
        } catch (SQLException e) {
            System.err.println("Error saving bed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Bed> findById(Long id) {
        String sql = "SELECT * FROM Beds WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBed(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding bed by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public List<Bed> findAll() {
        String sql = "SELECT * FROM Beds ORDER BY ward_name, room_number, bed_number";
        List<Bed> bedList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bedList.add(mapResultSetToBed(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all beds: " + e.getMessage());
        }
        
        return bedList;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Beds WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting bed: " + e.getMessage());
        }
    }

    @Override
    public Bed update(Bed bed) {
        String sql = "UPDATE Beds SET ward_name = ?, room_number = ?, bed_number = ?, occupied_by = ? WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, bed.getWardName());
            stmt.setString(2, bed.getRoomNumber());
            stmt.setString(3, bed.getBedNumber());
            stmt.setLong(4, bed.getOccupiedBy() != null ? bed.getOccupiedBy() : 0);
            stmt.setLong(5, bed.getBedId());
            
            stmt.executeUpdate();
            return bed;
            
        } catch (SQLException e) {
            System.err.println("Error updating bed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Bed> findVacantBeds() {
        String sql = "SELECT * FROM Beds WHERE occupied_by IS NULL OR occupied_by = 0 ORDER BY ward_name, room_number, bed_number";
        List<Bed> bedList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bedList.add(mapResultSetToBed(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding vacant beds: " + e.getMessage());
        }
        
        return bedList;
    }

    @Override
    public List<Bed> findOccupiedBeds() {
        String sql = "SELECT * FROM Beds WHERE occupied_by IS NOT NULL AND occupied_by > 0 ORDER BY ward_name, room_number, bed_number";
        List<Bed> bedList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bedList.add(mapResultSetToBed(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding occupied beds: " + e.getMessage());
        }
        
        return bedList;
    }

    @Override
    public List<Bed> findByWard(String wardName) {
        String sql = "SELECT * FROM Beds WHERE ward_name = ? ORDER BY room_number, bed_number";
        List<Bed> bedList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, wardName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bedList.add(mapResultSetToBed(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding beds by ward: " + e.getMessage());
        }
        
        return bedList;
    }

    @Override
    public void assignResidentToBed(Long bedId, Long residentId) {
        String sql = "UPDATE Beds SET occupied_by = ? WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, residentId);
            stmt.setLong(2, bedId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error assigning resident to bed: " + e.getMessage());
        }
    }

    @Override
    public void vacateBed(Long bedId) {
        String sql = "UPDATE Beds SET occupied_by = NULL WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bedId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error vacating bed: " + e.getMessage());
        }
    }

    @Override
    public boolean isBedAvailable(Long bedId) {
        String sql = "SELECT occupied_by FROM Beds WHERE bed_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, bedId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                long occupiedBy = rs.getLong("occupied_by");
                return occupiedBy == 0 || rs.wasNull();
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking bed availability: " + e.getMessage());
        }
        
        return false;
    }

    private Bed mapResultSetToBed(ResultSet rs) throws SQLException {
        Bed bed = new Bed();
        bed.setBedId(rs.getLong("bed_id"));
        bed.setWardName(rs.getString("ward_name"));
        bed.setRoomNumber(rs.getString("room_number"));
        bed.setBedNumber(rs.getString("bed_number"));
        
        long occupiedBy = rs.getLong("occupied_by");
        bed.setOccupiedBy(occupiedBy > 0 ? occupiedBy : null);
        
        return bed;
    }
}
