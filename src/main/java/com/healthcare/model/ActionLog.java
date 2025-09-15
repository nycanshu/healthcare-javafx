package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * ActionLog Entity - Records all actions performed by staff for auditing
 * Maps to the Actions_Log table in the database
 */
@Entity
@Table(name = "Actions_Log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long actionId;
    
    @Column(name = "staff_id")
    private Long staffId;
    
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "time")
    private LocalDateTime time;
    
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", insertable = false, updatable = false)
    private Staff staff;
    
    // Constructors
    public ActionLog(Long staffId, String action, String details) {
        this.staffId = staffId;
        this.action = action;
        this.details = details;
        this.time = LocalDateTime.now();
    }
    
    public ActionLog(Staff staff, String action, String details) {
        this.staff = staff;
        this.staffId = null; // Will be set by service layer
        this.action = action;
        this.details = details;
        this.time = LocalDateTime.now();
    }
    
    // Utility methods
    public String getFullDescription() {
        return "[" + time + "] " + action + 
               (details != null && !details.isEmpty() ? " - " + details : "");
    }
    
    @PrePersist
    protected void onCreate() {
        if (time == null) {
            time = LocalDateTime.now();
        }
    }
}
