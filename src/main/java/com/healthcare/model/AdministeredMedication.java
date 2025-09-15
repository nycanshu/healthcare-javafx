package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * AdministeredMedication Entity - Records when nurses administer medications
 * Maps to the Administered_Medication table in the database
 */
@Entity
@Table(name = "Administered_Medication")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministeredMedication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;
    
    @Column(name = "prescription_medicine_id", nullable = false)
    private Long prescriptionMedicineId;
    
    @Column(name = "nurse_id", nullable = false)
    private Long nurseId;
    
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_medicine_id", insertable = false, updatable = false)
    private PrescriptionMedicine prescriptionMedicine;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", insertable = false, updatable = false)
    private Staff nurse;
    
    // Constructors
    public AdministeredMedication(Long prescriptionMedicineId, Long nurseId, LocalDateTime time) {
        this.prescriptionMedicineId = prescriptionMedicineId;
        this.nurseId = nurseId;
        this.time = time;
    }
    
    // Utility methods
    public String getFullDescription() {
        return "Medication administered at " + time;
    }
}
