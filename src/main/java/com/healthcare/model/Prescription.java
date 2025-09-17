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
 * Prescription Entity - Represents a prescription written by a doctor for a resident
 * Maps to the Prescriptions table in the database
 */
@Entity
@Table(name = "Prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;
    
    @Column(name = "resident_id", nullable = false)
    private Long residentId;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @Column(name = "prescription_date", nullable = false)
    private LocalDate prescriptionDate;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PrescriptionStatus status = PrescriptionStatus.Active;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", insertable = false, updatable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private Staff doctor;
    
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();
    
    // Enums
    public enum PrescriptionStatus {
        Active, Completed, Cancelled
    }
    
    // Constructors
    public Prescription(Long residentId, Long doctorId, LocalDate prescriptionDate, String notes) {
        this.residentId = residentId;
        this.doctorId = doctorId;
        this.prescriptionDate = prescriptionDate;
        this.notes = notes;
        this.status = PrescriptionStatus.Active;
        this.createdAt = LocalDateTime.now();
    }
    
    // Utility methods
    public void addMedicine(PrescriptionMedicine prescriptionMedicine) {
        prescriptionMedicines.add(prescriptionMedicine);
    }
    
    public void removeMedicine(PrescriptionMedicine prescriptionMedicine) {
        prescriptionMedicines.remove(prescriptionMedicine);
    }
    
    public boolean hasMedicines() {
        return !prescriptionMedicines.isEmpty();
    }
    
    public int getMedicineCount() {
        return prescriptionMedicines.size();
    }
}
