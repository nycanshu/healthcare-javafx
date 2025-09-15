package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Staff Entity - Represents staff members (Manager, Doctor, Nurse)
 * Maps to the Staff table in the database
 */
@Entity
@Table(name = "Staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;
    
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    
    // Relationships
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "nurse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdministeredMedication> administeredMedications = new ArrayList<>();
    
    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActionLog> actionLogs = new ArrayList<>();
    
    // Constructors
    public Staff(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Utility methods
    public boolean isManager() {
        return role == Role.Manager;
    }
    
    public boolean isDoctor() {
        return role == Role.Doctor;
    }
    
    public boolean isNurse() {
        return role == Role.Nurse;
    }
    
    public boolean canAddPrescriptions() {
        return isDoctor();
    }
    
    public boolean canAdministerMedication() {
        return isNurse();
    }
    
    public boolean canManageStaff() {
        return isManager();
    }
    
    // Role Enum
    public enum Role {
        Manager, Doctor, Nurse
    }
}
