package com.healthcare.controller;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for the Manager Dashboard
 * Simple implementation with component-wise structure
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
        // No info message needed - directly navigate to staff management
    }
    
    @FXML
    private void admitNewResident() {
        showResidentManagement();
        showInfo("Admit New Resident", "Resident admission functionality will be implemented here.");
    }
    
    @FXML
    private void scheduleShifts() {
        showShiftScheduling();
        showInfo("Schedule Shifts", "Shift scheduling functionality will be implemented here.");
    }
    
    @FXML
    private void generateReport() {
        showReportsArchives();
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
        // Define consistent color scheme
        final String DEFAULT_COLOR = "#3498DB";      // Blue
        final String ACTIVE_COLOR = "#2C3E50";       // Dark blue-gray (active state)
        final String HOVER_COLOR = "#2980B9";        // Darker blue (hover state)
        
        // Base button style
        final String BASE_STYLE = "-fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 12 20 12 20; -fx-font-weight: bold; -fx-cursor: hand;";
        
        // Reset all buttons
        List<Button> buttons = List.of(
            dashboardButton, staffManagementButton, residentManagementButton,
            shiftSchedulingButton, actionLogsButton, reportsArchivesButton, systemSettingsButton
        );
        
        for (Button button : buttons) {
            if (button == activeButton) {
                // Active button - darker color with subtle shadow
                button.setStyle("-fx-background-color: " + ACTIVE_COLOR + "; " + BASE_STYLE + 
                               " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");
            } else {
                // Default button - consistent blue color
                button.setStyle("-fx-background-color: " + DEFAULT_COLOR + "; " + BASE_STYLE);
                
                // Add hover effect
                button.setOnMouseEntered(e -> {
                    if (button != activeButton) {
                        button.setStyle("-fx-background-color: " + HOVER_COLOR + "; " + BASE_STYLE);
                    }
                });
                
                button.setOnMouseExited(e -> {
                    if (button != activeButton) {
                        button.setStyle("-fx-background-color: " + DEFAULT_COLOR + "; " + BASE_STYLE);
                    }
                });
            }
        }
    }
    
    private void showContent(VBox content) {
        // Hide all content areas using both visible and managed properties
        // This prevents layout shifts and components from being pushed down
        dashboardContent.setVisible(false);
        dashboardContent.setManaged(false);
        
        staffManagementContent.setVisible(false);
        staffManagementContent.setManaged(false);
        
        residentManagementContent.setVisible(false);
        residentManagementContent.setManaged(false);
        
        shiftSchedulingContent.setVisible(false);
        shiftSchedulingContent.setManaged(false);
        
        actionLogsContent.setVisible(false);
        actionLogsContent.setManaged(false);
        
        reportsArchivesContent.setVisible(false);
        reportsArchivesContent.setManaged(false);
        
        systemSettingsContent.setVisible(false);
        systemSettingsContent.setManaged(false);
        
        // Show selected content
        content.setVisible(true);
        content.setManaged(true);
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