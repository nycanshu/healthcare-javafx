package com.healthcare.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main Controller for the Healthcare Application
 * Simple Hello World controller
 */
public class MainController implements Initializable {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private Button helloButton;
    
    @FXML
    private Label greetingLabel;
    
    private Stage primaryStage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing MainController...");
        
        // Set up event handlers
        setupEventHandlers();
        
        // Initialize UI
        initializeUI();
        
        System.out.println("MainController initialized successfully");
    }
    
    private void setupEventHandlers() {
        // Hello World functionality
        helloButton.setOnAction(event -> handleHelloButton());
    }
    
    private void initializeUI() {
        welcomeLabel.setText("Welcome to Healthcare Management System");
        greetingLabel.setText("Enter your name and click Hello!");
        greetingLabel.setVisible(false);
    }
    
    @FXML
    private void handleHelloButton() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            greetingLabel.setText("Please enter your name first!");
            greetingLabel.setVisible(true);
        } else {
            greetingLabel.setText("Hello, " + name + "! Welcome to the Healthcare System!");
            greetingLabel.setVisible(true);
            System.out.println("Hello button clicked for user: " + name);
        }
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
