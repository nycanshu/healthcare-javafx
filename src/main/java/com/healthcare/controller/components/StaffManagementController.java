package com.healthcare.controller.components;

import com.healthcare.model.Staff;
import com.healthcare.services.StaffService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Component Controller for Staff Management
 * Handles all staff CRUD operations
 */
public class StaffManagementController implements Initializable {
    
    // FXML Elements
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Long> staffIdColumn;
    @FXML private TableColumn<Staff, String> usernameColumn;
    @FXML private TableColumn<Staff, Staff.Role> roleColumn;
    @FXML private TableColumn<Staff, Void> actionsColumn;
    @FXML private VBox staffFormContainer;
    @FXML private Label formTitleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Staff.Role> roleComboBox;
    
    // New UI Elements
    @FXML private Label managersCountLabel;
    @FXML private Label doctorsCountLabel;
    @FXML private Label nursesCountLabel;
    @FXML private TextField searchField;
    
    // Data
    private ObservableList<Staff> staffList = FXCollections.observableArrayList();
    private FilteredList<Staff> filteredStaffList;
    private Staff editingStaff = null;
    
    // Service
    private StaffService staffService = new StaffService();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupSearch();
        loadStaffData();
    }
    
    private void setupTable() {
        System.out.println("Setting up staff management component...");
        
        // Setup table columns with equal width distribution
        staffIdColumn.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        staffIdColumn.prefWidthProperty().bind(staffTable.widthProperty().multiply(0.15));
        
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.prefWidthProperty().bind(staffTable.widthProperty().multiply(0.35));
        
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.prefWidthProperty().bind(staffTable.widthProperty().multiply(0.25));
        
        // Setup actions column with space-around layout
        actionsColumn.setCellFactory(col -> new TableCell<Staff, Void>() {
            private final Button editButton = new Button("✏️ Edit");
            private final Button deleteButton = new Button("🗑️ Delete");
            private final HBox buttonBox = new HBox(10, editButton, deleteButton);
            
            {
                // Style buttons
                editButton.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; " +
                                  "-fx-background-radius: 6; -fx-padding: 6 12; " +
                                  "-fx-font-weight: bold; -fx-font-size: 12px;");
                deleteButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; " +
                                    "-fx-background-radius: 6; -fx-padding: 6 12; " +
                                    "-fx-font-weight: bold; -fx-font-size: 12px;");
                
                // Set HBox to space buttons around
                buttonBox.setStyle("-fx-alignment: center; -fx-spacing: 10;");
                
                editButton.setOnAction(e -> {
                    Staff staff = getTableView().getItems().get(getIndex());
                    editStaff(staff);
                });
                
                deleteButton.setOnAction(e -> {
                    Staff staff = getTableView().getItems().get(getIndex());
                    deleteStaff(staff);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        actionsColumn.prefWidthProperty().bind(staffTable.widthProperty().multiply(0.25));
        
        // Setup role combo box
        roleComboBox.setItems(FXCollections.observableArrayList(Staff.Role.values()));
        
        System.out.println("Staff management component setup complete");
    }
    
    private void setupSearch() {
        // Create filtered list
        filteredStaffList = new FilteredList<>(staffList, p -> true);
        
        // Bind table to filtered list
        staffTable.setItems(filteredStaffList);
        
        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredStaffList.setPredicate(staff -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                return staff.getUsername().toLowerCase().contains(lowerCaseFilter) ||
                       staff.getRole().name().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
    
    @FXML
    private void showAddStaffForm() {
        editingStaff = null;
        formTitleLabel.setText("Add New Staff Member");
        clearForm();
        staffFormContainer.setVisible(true);
        staffFormContainer.setManaged(true);
    }
    
    @FXML
    private void refreshStaffList() {
        loadStaffData();
        showSuccess("Staff list refreshed successfully!");
    }
    
    @FXML
    private void saveStaff() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            Staff.Role role = roleComboBox.getValue();
            
            if (username.isEmpty() || password.isEmpty() || role == null) {
                showError("Please fill in all fields");
                return;
            }
            
            if (editingStaff == null) {
                // Add new staff
                Staff newStaff = new Staff(username, password, role);
                Staff savedStaff = staffService.save(newStaff);
                staffList.add(savedStaff);
                showSuccess("Staff member added successfully!");
            } else {
                // Update existing staff
                editingStaff.setUsername(username);
                editingStaff.setPassword(password);
                editingStaff.setRole(role);
                try {
                    staffService.update(editingStaff);
                    showSuccess("Staff member updated successfully!");
                } catch (Exception e) {
                    showError("Failed to update staff: " + e.getMessage());
                }
            }
            
            cancelStaffForm();
            loadStaffData();
            
        } catch (Exception e) {
            showError("Failed to save staff: " + e.getMessage());
        }
    }
    
    @FXML
    private void cancelStaffForm() {
        editingStaff = null;
        clearForm();
        staffFormContainer.setVisible(false);
        staffFormContainer.setManaged(false);
    }
    
    private void editStaff(Staff staff) {
        editingStaff = staff;
        formTitleLabel.setText("Edit Staff Member");
        
        usernameField.setText(staff.getUsername());
        passwordField.setText(staff.getPassword());
        roleComboBox.setValue(staff.getRole());
        
        staffFormContainer.setVisible(true);
        staffFormContainer.setManaged(true);
    }
    
    private void deleteStaff(Staff staff) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Staff Member");
        confirmAlert.setContentText("Are you sure you want to delete " + staff.getUsername() + "?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    staffService.deleteById(staff.getStaffId());
                    staffList.remove(staff);
                    showSuccess("Staff member deleted successfully!");
                } catch (Exception e) {
                    showError("Failed to delete staff: " + e.getMessage());
                }
            }
        });
    }
    
    private void loadStaffData() {
        try {
            System.out.println("Loading staff data in component...");
            staffList.clear();
            List<Staff> allStaff = staffService.findAll();
            System.out.println("Found " + allStaff.size() + " staff members");
            
            for (Staff staff : allStaff) {
                System.out.println("Staff: ID=" + staff.getStaffId() + 
                                 ", Username=" + staff.getUsername() + 
                                 ", Role=" + staff.getRole());
            }
            
            staffList.addAll(allStaff);
            staffTable.refresh();
            updateStaffCounts();
            System.out.println("Staff table updated with " + staffList.size() + " items");
            
        } catch (Exception e) {
            System.err.println("Error loading staff data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateStaffCounts() {
        long managersCount = staffList.stream()
            .filter(staff -> staff.getRole() == Staff.Role.Manager)
            .count();
        
        long doctorsCount = staffList.stream()
            .filter(staff -> staff.getRole() == Staff.Role.Doctor)
            .count();
        
        long nursesCount = staffList.stream()
            .filter(staff -> staff.getRole() == Staff.Role.Nurse)
            .count();
        
        managersCountLabel.setText(String.valueOf(managersCount));
        doctorsCountLabel.setText(String.valueOf(doctorsCount));
        nursesCountLabel.setText(String.valueOf(nursesCount));
    }
    
    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
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
