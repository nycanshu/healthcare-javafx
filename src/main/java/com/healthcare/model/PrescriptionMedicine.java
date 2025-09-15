package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * PrescriptionMedicine Entity - Links prescriptions to medicines with dosage and schedule
 * Maps to the Prescription_Medicines table in the database
 */
@Entity
@Table(name = "Prescription_Medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionMedicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;
    
    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;
    
    @Column(name = "dosage")
    private String dosage;
    
    @Column(name = "schedule")
    private String schedule; // e.g., "8am, 2pm"
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", insertable = false, updatable = false)
    private Prescription prescription;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine;
    
    @OneToMany(mappedBy = "prescriptionMedicine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdministeredMedication> administeredMedications = new ArrayList<>();
    
    // Constructors
    public PrescriptionMedicine(Long prescriptionId, Long medicineId, String dosage, String schedule) {
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.schedule = schedule;
    }
    
    // Utility methods
    public void addAdministration(AdministeredMedication administeredMedication) {
        administeredMedications.add(administeredMedication);
    }
    
    public void removeAdministration(AdministeredMedication administeredMedication) {
        administeredMedications.remove(administeredMedication);
    }
    
    public boolean hasBeenAdministered() {
        return !administeredMedications.isEmpty();
    }
    
    public int getAdministrationCount() {
        return administeredMedications.size();
    }
    
    public String getMedicineName() {
        return "Medicine";
    }
    
    public String getFullDescription() {
        return getMedicineName() + " - " + dosage + " - " + schedule;
    }
}
