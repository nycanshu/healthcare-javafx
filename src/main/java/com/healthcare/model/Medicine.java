package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Medicine Entity - Represents available medicines in the system
 * Maps to the Medicines table in the database
 */
@Entity
@Table(name = "Medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Long medicineId;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "dosage_unit")
    private String dosageUnit = "mg";
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();
    
    // Constructors
    public Medicine(String name) {
        this.name = name;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
    
    public Medicine(String name, String description, String dosageUnit) {
        this.name = name;
        this.description = description;
        this.dosageUnit = dosageUnit;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Utility methods
    public void addToPrescription(PrescriptionMedicine prescriptionMedicine) {
        prescriptionMedicines.add(prescriptionMedicine);
    }
    
    public void removeFromPrescription(PrescriptionMedicine prescriptionMedicine) {
        prescriptionMedicines.remove(prescriptionMedicine);
    }
}
