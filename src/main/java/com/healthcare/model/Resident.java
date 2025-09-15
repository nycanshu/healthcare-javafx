package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Resident Entity - Represents a high care patient in the care home
 * Maps to the Residents table in the database
 */
@Entity
@Table(name = "Residents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resident_id")
    private Long residentId;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;
    
    @Column(name = "discharge_date")
    private LocalDate dischargeDate;
    
    @Column(name = "current_bed_id")
    private Long currentBedId;
    
    // Relationships
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    // Constructors
    public Resident() {
        // Default no-args constructor
    }
    
    public Resident(String firstName, String lastName, Gender gender, LocalDate admissionDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.admissionDate = admissionDate;
    }
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public int getAge() {
        if (birthDate == null) return 0;
        return LocalDate.now().getYear() - birthDate.getYear();
    }
    
    public boolean isDischarged() {
        return dischargeDate != null;
    }
    
    public void discharge() {
        this.dischargeDate = LocalDate.now();
        this.currentBedId = null;
    }
    
    // Gender Enum
    public enum Gender {
        M, F
    }
    
    // Manual getters and setters (since Lombok might not be working consistently)
    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    
    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }
    
    public Long getCurrentBedId() { return currentBedId; }
    public void setCurrentBedId(Long currentBedId) { this.currentBedId = currentBedId; }
}
