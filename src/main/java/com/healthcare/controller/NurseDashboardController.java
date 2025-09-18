package com.healthcare.controller;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for the Nurse Dashboard
 * Simple implementation with component-wise structure
 */
public class NurseDashboardController extends BaseDashboardController {
    
    // Dashboard elements
    @FXML
    private Label totalResidentsLabel;
    @FXML
    private Label myPatientsLabel;
    @FXML
    private Label todaysTasksLabel;
    @FXML
    private Label pendingMedicationsLabel;
    @FXML
    private ListView<String> recentActivityList;
    
    // Navigation buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button patientCareButton;
    @FXML
    private Button medicationsButton;
    @FXML
    private Button reportsButton;
    
    // Content areas
    @FXML
    private VBox mainContentArea;
    @FXML
    private VBox dashboardContent;
    @FXML
    private VBox patientCareContent;
    @FXML
    private VBox medicationsContent;
    @FXML
    private VBox reportsContent;
    
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
    private void showPatientCare() {
        setActiveButton(patientCareButton);
        showContent(patientCareContent);
        loadPatientCareData();
    }
    
    @FXML
    private void showMedications() {
        setActiveButton(medicationsButton);
        showContent(medicationsContent);
        loadMedicationData();
    }
    
    @FXML
    private void showReports() {
        setActiveButton(reportsButton);
        showContent(reportsContent);
        loadReportsData();
    }
    
    // Data loading methods
    private void loadDashboardData() {
        try {
            // Load statistics using simple services
            long totalStaff = staffService.findAll().size();
            
            totalResidentsLabel.setText("0"); // TODO: Implement ResidentService
            myPatientsLabel.setText("0"); // TODO: Implement Nurse-specific patient count
            todaysTasksLabel.setText("0"); // TODO: Implement task service
            pendingMedicationsLabel.setText("0"); // TODO: Implement medication service
            
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
            "Nurse logged in",
            "Dashboard loaded"
        );
    }
    
    private void loadPatientCareData() {
        try {
            // TODO: Load patient care component
            patientCareContent.getChildren().clear();
            Label placeholder = new Label("Patient Care Management - Coming Soon");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            patientCareContent.getChildren().add(placeholder);
            
            System.out.println("Patient care component loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading patient care component: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadMedicationData() {
        try {
            // TODO: Load medication component
            medicationsContent.getChildren().clear();
            Label placeholder = new Label("Medication Management - Coming Soon");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            medicationsContent.getChildren().add(placeholder);
            
            System.out.println("Medication component loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading medication component: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadReportsData() {
        try {
            // TODO: Load reports component
            reportsContent.getChildren().clear();
            Label placeholder = new Label("Reports - Coming Soon");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            reportsContent.getChildren().add(placeholder);
            
            System.out.println("Reports component loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading reports component: " + e.getMessage());
            e.printStackTrace();
        }
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
            dashboardButton, patientCareButton, medicationsButton, reportsButton
        );
        
        for (Button button : buttons) {
            if (button == activeButton) {
                // Active button - darker color with subtle shadow
                button.setStyle("-fx-background-color: " + ACTIVE_COLOR + "; " + BASE_STYLE + 
                               " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");
            } else {
                // Default button - consistent blue color
                button.setStyle("-fx-background-color: " + DEFAULT_COLOR + "; " + BASE_STYLE);
            }
            
            // Add hover effects
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
    
    private void showContent(VBox content) {
        // Hide all content areas
        List<VBox> contentAreas = List.of(
            dashboardContent, patientCareContent, medicationsContent, reportsContent
        );
        
        for (VBox area : contentAreas) {
            area.setVisible(false);
            area.setManaged(false);
        }
        
        // Show selected content
        content.setVisible(true);
        content.setManaged(true);
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
