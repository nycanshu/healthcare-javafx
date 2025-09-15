package com.healthcare.services;

import com.healthcare.config.DBConnection;
import com.healthcare.model.Staff;
import com.healthcare.services.impl.IStaffService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple StaffService implementation using MySQL CRUD operations
 */
public class StaffService implements IStaffService {

    @Override
    public Staff save(Staff staff) {
        String sql = "INSERT INTO Staff (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPassword());
            stmt.setString(3, staff.getRole().toString());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    staff.setStaffId(generatedKeys.getLong(1));
                }
            }
            return staff;
            
        } catch (SQLException e) {
            System.err.println("Error saving staff: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Staff> findById(Long id) {
        String sql = "SELECT * FROM Staff WHERE staff_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getLong("staff_id"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setRole(Staff.Role.valueOf(rs.getString("role")));
                return Optional.of(staff);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding staff by ID: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public List<Staff> findAll() {
        String sql = "SELECT * FROM Staff";
        List<Staff> staffList = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getLong("staff_id"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setRole(Staff.Role.valueOf(rs.getString("role")));
                staffList.add(staff);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all staff: " + e.getMessage());
        }
        
        return staffList;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Staff WHERE staff_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error deleting staff: " + e.getMessage());
        }
    }

    @Override
    public Optional<Staff> authenticate(String username, String password) {
        String sql = "SELECT * FROM Staff WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffId(rs.getLong("staff_id"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));
                staff.setRole(Staff.Role.valueOf(rs.getString("role")));
                return Optional.of(staff);
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating staff: " + e.getMessage());
        }
        
        return Optional.empty();
    }


    @Override
    public Staff update(Staff staff) {
        String sql = "UPDATE Staff SET username = ?, password = ?, role = ? WHERE staff_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPassword());
            stmt.setString(3, staff.getRole().toString());
            stmt.setLong(4, staff.getStaffId());
            stmt.executeUpdate();
            return staff;
        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            return null;
        }
    }
}

        
