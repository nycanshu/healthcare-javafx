package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Bed Entity - Represents individual beds in rooms
 * Maps to the Beds table in the database
 */
@Entity
@Table(name = "Beds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Long bedId;
    
    @Column(name = "ward_name", nullable = false)
    private String wardName;
    
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @Column(name = "bed_number", nullable = false)
    private String bedNumber;
    
    @Column(name = "occupied_by")
    private Long occupiedBy;
    
    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupied_by", insertable = false, updatable = false)
    private Resident resident;
    
    // Constructors
    public Bed() {
        // Default no-args constructor
    }
    
    public Bed(String wardName, String roomNumber, String bedNumber) {
        this.wardName = wardName;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
        this.occupiedBy = null;
    }
    
    // Utility methods
    public boolean isOccupied() {
        return occupiedBy != null;
    }
    
    public boolean isVacant() {
        return occupiedBy == null;
    }
    
    public void assignResident(Resident resident) {
        this.occupiedBy = null; // Will be set by service layer
        this.resident = resident;
    }
    
    public void vacate() {
        this.occupiedBy = null;
        this.resident = null;
    }
    
    public String getLocation() {
        return wardName + " - Room " + roomNumber + " - Bed " + bedNumber;
    }
    
    public String getFullIdentifier() {
        return wardName + "_" + roomNumber + "_" + bedNumber;
    }
    
    // Manual getters and setters (since Lombok might not be working consistently)
    public Long getBedId() { return bedId; }
    public void setBedId(Long bedId) { this.bedId = bedId; }
    
    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }
    
    public Long getOccupiedBy() { return occupiedBy; }
    public void setOccupiedBy(Long occupiedBy) { this.occupiedBy = occupiedBy; }
}
