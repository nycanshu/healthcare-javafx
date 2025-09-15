package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
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
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", insertable = false, updatable = false)
    private Resident resident;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private Staff doctor;
    
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();
    
    // Constructors
    public Prescription(Long residentId, Long doctorId, LocalDate date, String notes) {
        this.residentId = residentId;
        this.doctorId = doctorId;
        this.date = date;
        this.notes = notes;
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
