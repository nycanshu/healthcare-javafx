package com.healthcare.repo;

import com.healthcare.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Simple repository for ActionLog entity - Basic CRUD operations only
 */
@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {
    
    // Find by staff
    List<ActionLog> findByStaffId(Long staffId);
    
    // Find by action
    List<ActionLog> findByAction(String action);
}