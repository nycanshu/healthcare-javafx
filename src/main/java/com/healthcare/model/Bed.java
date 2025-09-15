package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Enhanced Bed Entity - Represents individual beds in rooms
 * Maps to the improved Beds table in the database
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
    
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    
    @Column(name = "bed_number", nullable = false)
    private String bedNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type")
    private BedType bedType;
    
    @Column(name = "is_occupied")
    private boolean occupied;
    
    @Column(name = "occupied_by")
    private Long occupiedBy;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_restriction")
    private GenderRestriction genderRestriction;
    
    @Column(name = "isolation_required")
    private boolean isolationRequired;
    
    @Column(name = "last_cleaned")
    private LocalDateTime lastCleaned;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupied_by", insertable = false, updatable = false)
    private Resident resident;
    
    // Constructors
    public Bed() {
        // Default no-args constructor
    }
    
    public Bed(Long roomId, String bedNumber, BedType bedType) {
        this.roomId = roomId;
        this.bedNumber = bedNumber;
        this.bedType = bedType;
        this.occupied = false;
        this.occupiedBy = null;
        this.genderRestriction = GenderRestriction.None;
        this.isolationRequired = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Utility methods
    public boolean isOccupied() {
        return occupied;
    }
    
    public boolean isVacant() {
        return !occupied;
    }
    
    public void assignResident(Resident resident) {
        this.occupied = true;
        this.occupiedBy = resident.getResidentId();
        this.resident = resident;
    }
    
    public void vacate() {
        this.occupied = false;
        this.occupiedBy = null;
        this.resident = null;
    }
    
    public String getLocation() {
        if (room != null) {
            return "Room " + room.getRoomNumber() + " - Bed " + bedNumber;
        }
        return "Bed " + bedNumber;
    }
    
    public String getFullIdentifier() {
        if (room != null) {
            return "Room_" + room.getRoomNumber() + "_Bed_" + bedNumber;
        }
        return "Bed_" + bedNumber;
    }
    
    public String getRoomNumber() {
        return room != null ? room.getRoomNumber() : "Unknown";
    }
    
    public void setRoomNumber(String roomNumber) {
        // This is a transient field, so we don't need to store it
        // It's set by the service layer when loading from database
    }
    
    public String getDisplayName() {
        return "Bed " + bedNumber + " (" + bedType + ")";
    }
    
    public boolean isSuitableForGender(Resident.Gender gender) {
        return genderRestriction == GenderRestriction.None || 
               genderRestriction.name().equals(gender.name());
    }
    
    public boolean isSuitableForIsolation(boolean requiresIsolation) {
        return !requiresIsolation || isolationRequired;
    }
    
    // Enums
    public enum BedType {
        Standard, Electric, Special
    }
    
    public enum GenderRestriction {
        None, Male, Female
    }
    
    // Manual getters and setters (since Lombok might not be working consistently)
    public Long getBedId() { return bedId; }
    public void setBedId(Long bedId) { this.bedId = bedId; }
    
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    
    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }
    
    public BedType getBedType() { return bedType; }
    public void setBedType(BedType bedType) { this.bedType = bedType; }
    
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    
    public Long getOccupiedBy() { return occupiedBy; }
    public void setOccupiedBy(Long occupiedBy) { this.occupiedBy = occupiedBy; }
    
    public GenderRestriction getGenderRestriction() { return genderRestriction; }
    public void setGenderRestriction(GenderRestriction genderRestriction) { this.genderRestriction = genderRestriction; }
    
    public boolean isIsolationRequired() { return isolationRequired; }
    public void setIsolationRequired(boolean isolationRequired) { this.isolationRequired = isolationRequired; }
    
    public LocalDateTime getLastCleaned() { return lastCleaned; }
    public void setLastCleaned(LocalDateTime lastCleaned) { this.lastCleaned = lastCleaned; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
}
