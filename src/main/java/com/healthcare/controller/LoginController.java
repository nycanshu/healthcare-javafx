package com.healthcare.controller;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the login screen
 * Handles user authentication and role-based navigation
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private ProgressIndicator loadingIndicator;
    
    private Stage primaryStage;
    private StaffService staffService = new StaffService(); // Simple instantiation

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.setOnAction(event -> handleLogin());
        
        // Enter key support
        usernameField.setOnAction(event -> passwordField.requestFocus());
        passwordField.setOnAction(event -> handleLogin());
        
        // Clear error when typing
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> clearError());
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        setLoading(true);
        
        // JPA Service authentication
        new Thread(() -> {
            try {
                Optional<Staff> staff = staffService.authenticate(username, password);
                
                Platform.runLater(() -> {
                    setLoading(false);
                    
                    if (staff.isPresent()) {
                        navigateToDashboard(staff.get());
                    } else {
                        showError("Invalid username or password");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    setLoading(false);
                    showError("Login failed: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void navigateToDashboard(Staff staff) {
        try {
            String dashboardFxml = getDashboardFxml(staff.getRole());
            FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFxml));
            Scene scene = new Scene(loader.load(), 1200, 800);
            
            // Set up the dashboard controller
            Object controller = loader.getController();
            if (controller instanceof BaseDashboardController) {
                ((BaseDashboardController) controller).setCurrentStaff(staff);
                ((BaseDashboardController) controller).setPrimaryStage(primaryStage);
            }
            
            primaryStage.setTitle("Healthcare Management System - " + staff.getRole() + " Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (IOException e) {
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private String getDashboardFxml(Staff.Role role) {
        switch (role) {
            case Manager:
                return "/fxml/manager-dashboard.fxml";
            case Doctor:
                return "/fxml/doctor-dashboard.fxml";
            case Nurse:
                return "/fxml/nurse-dashboard.fxml";
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    
    private void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
        loginButton.setDisable(loading);
        usernameField.setDisable(loading);
        passwordField.setDisable(loading);
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void clearError() {
        errorLabel.setVisible(false);
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
