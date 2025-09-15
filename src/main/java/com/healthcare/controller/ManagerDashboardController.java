package com.healthcare.controller;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for the Manager Dashboard
 * Simple implementation without Spring complexity
 */
public class ManagerDashboardController extends BaseDashboardController {
    
    // Dashboard elements
    @FXML
    private Label totalResidentsLabel;
    @FXML
    private Label totalStaffLabel;
    @FXML
    private Label occupiedBedsLabel;
    @FXML
    private Label todaysActionsLabel;
    @FXML
    private ListView<String> recentActivityList;
    
    // Navigation buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button staffManagementButton;
    @FXML
    private Button residentManagementButton;
    @FXML
    private Button shiftSchedulingButton;
    @FXML
    private Button actionLogsButton;
    @FXML
    private Button reportsArchivesButton;
    @FXML
    private Button systemSettingsButton;
    
    // Content areas
    @FXML
    private VBox mainContentArea;
    @FXML
    private VBox dashboardContent;
    @FXML
    private VBox staffManagementContent;
    @FXML
    private VBox residentManagementContent;
    @FXML
    private VBox shiftSchedulingContent;
    @FXML
    private VBox actionLogsContent;
    @FXML
    private VBox reportsArchivesContent;
    @FXML
    private VBox systemSettingsContent;
    
    // Services
    private StaffService staffService = new StaffService();

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        super.initialize(location, resources);
        setupNavigation();
        loadDashboardData();
    }
    
    private void setupNavigation() {
        // Set default active button
        setActiveButton(dashboardButton);
        showContent(dashboardContent);
    }
    
    // Navigation methods
    @FXML
    private void showDashboard() {
        setActiveButton(dashboardButton);
        showContent(dashboardContent);
        loadDashboardData();
    }
    
    @FXML
    private void showStaffManagement() {
        setActiveButton(staffManagementButton);
        showContent(staffManagementContent);
        loadStaffManagementData();
    }
    
    @FXML
    private void showResidentManagement() {
        setActiveButton(residentManagementButton);
        showContent(residentManagementContent);
        loadResidentManagementData();
    }
    
    @FXML
    private void showShiftScheduling() {
        setActiveButton(shiftSchedulingButton);
        showContent(shiftSchedulingContent);
        loadShiftSchedulingData();
    }
    
    @FXML
    private void showActionLogs() {
        setActiveButton(actionLogsButton);
        showContent(actionLogsContent);
        loadActionLogsData();
    }
    
    @FXML
    private void showReportsArchives() {
        setActiveButton(reportsArchivesButton);
        showContent(reportsArchivesContent);
        loadReportsArchivesData();
    }
    
    @FXML
    private void showSystemSettings() {
        setActiveButton(systemSettingsButton);
        showContent(systemSettingsContent);
        loadSystemSettingsData();
    }
    
    // Quick Actions
    @FXML
    private void addNewStaff() {
        showStaffManagement();
        // TODO: Open add staff dialog
        showInfo("Add New Staff", "Staff management functionality will be implemented here.");
    }
    
    @FXML
    private void admitNewResident() {
        showResidentManagement();
        // TODO: Open admit resident dialog
        showInfo("Admit New Resident", "Resident admission functionality will be implemented here.");
    }
    
    @FXML
    private void scheduleShifts() {
        showShiftScheduling();
        // TODO: Open shift scheduling dialog
        showInfo("Schedule Shifts", "Shift scheduling functionality will be implemented here.");
    }
    
    @FXML
    private void generateReport() {
        showReportsArchives();
        // TODO: Open report generation dialog
        showInfo("Generate Report", "Report generation functionality will be implemented here.");
    }
    
    // Data loading methods
    private void loadDashboardData() {
        try {
            // Load statistics using simple services
            long totalStaff = staffService.findAll().size();
            
            totalResidentsLabel.setText("0"); // TODO: Implement ResidentService
            totalStaffLabel.setText(String.valueOf(totalStaff));
            occupiedBedsLabel.setText("0"); // TODO: Implement BedService
            todaysActionsLabel.setText("0"); // TODO: Implement ActionLogService
            
            // Load recent activity
            loadRecentActivity();
            
        } catch (Exception e) {
            showError("Failed to load dashboard data: " + e.getMessage());
        }
    }
    
    private void loadRecentActivity() {
        recentActivityList.getItems().clear();
        recentActivityList.getItems().addAll(
            "System started successfully",
            "Manager logged in",
            "Dashboard loaded"
        );
    }
    
    private void loadStaffManagementData() {
        // TODO: Load staff management data
    }
    
    private void loadResidentManagementData() {
        // TODO: Load resident management data
    }
    
    private void loadShiftSchedulingData() {
        // TODO: Load shift scheduling data
    }
    
    private void loadActionLogsData() {
        // TODO: Load action logs data
    }
    
    private void loadReportsArchivesData() {
        // TODO: Load reports and archives data
    }
    
    private void loadSystemSettingsData() {
        // TODO: Load system settings data
    }
    
    // Helper methods
    private void setActiveButton(Button activeButton) {
        // Reset all buttons
        List<Button> buttons = List.of(
            dashboardButton, staffManagementButton, residentManagementButton,
            shiftSchedulingButton, actionLogsButton, reportsArchivesButton, systemSettingsButton
        );
        
        for (Button button : buttons) {
            if (button == activeButton) {
                button.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-background-radius: 3;");
            } else {
                // Reset to original colors based on button
                if (button == dashboardButton) {
                    button.setStyle("-fx-background-color: #2E86AB; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == staffManagementButton) {
                    button.setStyle("-fx-background-color: #A23B72; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == residentManagementButton) {
                    button.setStyle("-fx-background-color: #F18F01; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == shiftSchedulingButton) {
                    button.setStyle("-fx-background-color: #C73E1D; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == actionLogsButton) {
                    button.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == reportsArchivesButton) {
                    button.setStyle("-fx-background-color: #9B59B6; -fx-text-fill: white; -fx-background-radius: 3;");
                } else if (button == systemSettingsButton) {
                    button.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-background-radius: 3;");
                }
            }
        }
    }
    
    private void showContent(VBox content) {
        // Hide all content areas
        dashboardContent.setVisible(false);
        staffManagementContent.setVisible(false);
        residentManagementContent.setVisible(false);
        shiftSchedulingContent.setVisible(false);
        actionLogsContent.setVisible(false);
        reportsArchivesContent.setVisible(false);
        systemSettingsContent.setVisible(false);
        
        // Show selected content
        content.setVisible(true);
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
