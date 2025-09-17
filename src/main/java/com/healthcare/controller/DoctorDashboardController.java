package com.healthcare.controller;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for the Doctor Dashboard
 * Simple implementation with component-wise structure
 */
public class DoctorDashboardController extends BaseDashboardController {
    
    // Dashboard elements
    @FXML
    private Label totalResidentsLabel;
    @FXML
    private Label myPatientsLabel;
    @FXML
    private Label todaysAppointmentsLabel;
    @FXML
    private Label pendingPrescriptionsLabel;
    @FXML
    private ListView<String> recentActivityList;
    
    // Navigation buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button patientManagementButton;
    @FXML
    private Button prescriptionsButton;
    @FXML
    private Button reportsButton;
    
    // Content areas
    @FXML
    private VBox mainContentArea;
    @FXML
    private VBox dashboardContent;
    @FXML
    private VBox patientManagementContent;
    @FXML
    private VBox prescriptionsContent;
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
    private void showPatientManagement() {
        setActiveButton(patientManagementButton);
        showContent(patientManagementContent);
        loadPatientManagementData();
    }
    
    @FXML
    private void showPrescriptions() {
        setActiveButton(prescriptionsButton);
        showContent(prescriptionsContent);
        loadPrescriptionsData();
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
            myPatientsLabel.setText("0"); // TODO: Implement Doctor-specific patient count
            todaysAppointmentsLabel.setText("0"); // TODO: Implement appointment service
            pendingPrescriptionsLabel.setText("0"); // TODO: Implement prescription service
            
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
            "Doctor logged in",
            "Dashboard loaded"
        );
    }
    
    private void loadPatientManagementData() {
        try {
            // TODO: Load patient management component
            patientManagementContent.getChildren().clear();
            Label placeholder = new Label("Patient Management - Coming Soon");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            patientManagementContent.getChildren().add(placeholder);
            
            System.out.println("Patient management component loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading patient management component: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadPrescriptionsData() {
        try {
            // TODO: Load prescriptions component
            prescriptionsContent.getChildren().clear();
            Label placeholder = new Label("Prescriptions Management - Coming Soon");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            prescriptionsContent.getChildren().add(placeholder);
            
            System.out.println("Prescriptions component loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading prescriptions component: " + e.getMessage());
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
            dashboardButton, patientManagementButton, prescriptionsButton, reportsButton
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
            dashboardContent, patientManagementContent, prescriptionsContent, reportsContent
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
