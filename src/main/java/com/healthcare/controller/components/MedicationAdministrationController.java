package com.healthcare.controller.components;

import com.healthcare.model.Staff;
import com.healthcare.services.MedicationAdministrationService;
import com.healthcare.services.MedicationAdministrationService.MedicationSchedule;
import com.healthcare.services.MedicationAdministrationService.MedicationStats;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Medication Administration component
 * Allows nurses to view and manage medication schedules
 */
public class MedicationAdministrationController implements Initializable {
    
    @FXML
    private VBox mainContainer;
    
    @FXML
    private Label totalScheduledLabel;
    @FXML
    private Label administeredTodayLabel;
    @FXML
    private Label pendingLabel;
    @FXML
    private Label overdueLabel;
    
    @FXML
    private Tab scheduledTab;
    @FXML
    private Tab pendingTab;
    @FXML
    private Tab administeredTab;
    @FXML
    private Tab overdueTab;
    
    @FXML
    private TableView<MedicationSchedule> scheduledTable;
    @FXML
    private TableColumn<MedicationSchedule, String> patientNameColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> medicineColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> dosageColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> frequencyColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> bedColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> doctorColumn;
    
    @FXML
    private TableView<MedicationSchedule> pendingTable;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingPatientColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingMedicineColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingDosageColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingFrequencyColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingBedColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> pendingDoctorColumn;
    
    @FXML
    private TableView<MedicationSchedule> administeredTable;
    @FXML
    private TableColumn<MedicationSchedule, String> adminPatientColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> adminMedicineColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> adminDosageColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> adminTimeColumn;
    @FXML
    private TableColumn<MedicationSchedule, String> adminNurseColumn;
    
    @FXML
    private Button refreshButton;
    @FXML
    private Button markAdministeredButton;
    @FXML
    private Button markMissedButton;
    @FXML
    private Button markRefusedButton;
    
    @FXML
    private TextArea notesTextArea;
    @FXML
    private TextField dosageGivenField;
    
    // Services
    private MedicationAdministrationService medicationService = new MedicationAdministrationService();
    private Staff currentNurse;
    
    // Data
    private ObservableList<MedicationSchedule> scheduledData = FXCollections.observableArrayList();
    private ObservableList<MedicationSchedule> pendingData = FXCollections.observableArrayList();
    private ObservableList<MedicationSchedule> administeredData = FXCollections.observableArrayList();
    private ObservableList<MedicationSchedule> overdueData = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("MedicationAdministrationController: Initializing...");
        setupTableColumns();
        setupEventHandlers();
        loadData();
        System.out.println("MedicationAdministrationController: Initialization complete");
    }
    
    private void setupTableColumns() {
        // Scheduled medications table
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        bedColumn.setCellValueFactory(cellData -> {
            Long bedId = cellData.getValue().getBedId();
            return new javafx.beans.property.SimpleStringProperty("Bed " + (bedId != null ? bedId : "N/A"));
        });
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        
        // Pending medications table
        pendingPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        pendingMedicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        pendingDosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        pendingFrequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        pendingBedColumn.setCellValueFactory(cellData -> {
            Long bedId = cellData.getValue().getBedId();
            return new javafx.beans.property.SimpleStringProperty("Bed " + (bedId != null ? bedId : "N/A"));
        });
        pendingDoctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        
        // Administered medications table
        adminPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        adminMedicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        adminDosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        adminTimeColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        });
        adminNurseColumn.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty(
                currentNurse != null ? currentNurse.getFullName() : "Nurse"
            );
        });
    }
    
    private void setupEventHandlers() {
        refreshButton.setOnAction(e -> loadData());
        
        markAdministeredButton.setOnAction(e -> markSelectedAsAdministered());
        markMissedButton.setOnAction(e -> markSelectedAsMissed());
        markRefusedButton.setOnAction(e -> markSelectedAsRefused());
        
        // Enable/disable buttons based on selection
        scheduledTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            markAdministeredButton.setDisable(!hasSelection);
            markMissedButton.setDisable(!hasSelection);
            markRefusedButton.setDisable(!hasSelection);
        });
        
        pendingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            markAdministeredButton.setDisable(!hasSelection);
            markMissedButton.setDisable(!hasSelection);
            markRefusedButton.setDisable(!hasSelection);
        });
    }
    
    private void loadData() {
        try {
            // Load statistics
            MedicationStats stats = medicationService.getMedicationStats();
            totalScheduledLabel.setText(String.valueOf(stats.getTotalScheduled()));
            administeredTodayLabel.setText(String.valueOf(stats.getAdministeredToday()));
            pendingLabel.setText(String.valueOf(stats.getPending()));
            overdueLabel.setText(String.valueOf(stats.getOverdue()));
            
            // Load scheduled medications
            List<MedicationSchedule> scheduled = medicationService.getTodaysMedicationSchedule();
            scheduledData.clear();
            scheduledData.addAll(scheduled);
            scheduledTable.setItems(scheduledData);
            
            // Load pending medications
            List<MedicationSchedule> pending = medicationService.getPendingMedications();
            pendingData.clear();
            pendingData.addAll(pending);
            pendingTable.setItems(pendingData);
            
            // Load overdue medications
            List<MedicationSchedule> overdue = medicationService.getOverdueMedications();
            overdueData.clear();
            overdueData.addAll(overdue);
            
            System.out.println("Loaded " + scheduled.size() + " scheduled, " + pending.size() + " pending medications");
            
        } catch (Exception e) {
            System.err.println("Error loading medication data: " + e.getMessage());
            showError("Failed to load medication data: " + e.getMessage());
        }
    }
    
    private void markSelectedAsAdministered() {
        MedicationSchedule selected = getSelectedMedication();
        if (selected == null) {
            showAlert("Please select a medication to mark as administered.");
            return;
        }
        
        String dosageGiven = dosageGivenField.getText().trim();
        if (dosageGiven.isEmpty()) {
            dosageGiven = selected.getDosage(); // Use prescribed dosage as default
        }
        
        String notes = notesTextArea.getText().trim();
        
        boolean success = medicationService.markMedicationAsAdministered(
            selected.getPrescriptionMedicineId(),
            currentNurse.getStaffId(),
            dosageGiven,
            notes
        );
        
        if (success) {
            showAlert("Medication marked as administered successfully.");
            clearFields();
            loadData();
        } else {
            showAlert("Failed to mark medication as administered.");
        }
    }
    
    private void markSelectedAsMissed() {
        MedicationSchedule selected = getSelectedMedication();
        if (selected == null) {
            showAlert("Please select a medication to mark as missed.");
            return;
        }
        
        String notes = notesTextArea.getText().trim();
        if (notes.isEmpty()) {
            notes = "Medication missed - no reason provided";
        }
        
        boolean success = medicationService.markMedicationAsMissed(
            selected.getPrescriptionMedicineId(),
            currentNurse.getStaffId(),
            notes
        );
        
        if (success) {
            showAlert("Medication marked as missed successfully.");
            clearFields();
            loadData();
        } else {
            showAlert("Failed to mark medication as missed.");
        }
    }
    
    private void markSelectedAsRefused() {
        MedicationSchedule selected = getSelectedMedication();
        if (selected == null) {
            showAlert("Please select a medication to mark as refused.");
            return;
        }
        
        String notes = notesTextArea.getText().trim();
        if (notes.isEmpty()) {
            notes = "Medication refused by patient";
        }
        
        boolean success = medicationService.markMedicationAsRefused(
            selected.getPrescriptionMedicineId(),
            currentNurse.getStaffId(),
            notes
        );
        
        if (success) {
            showAlert("Medication marked as refused successfully.");
            clearFields();
            loadData();
        } else {
            showAlert("Failed to mark medication as refused.");
        }
    }
    
    private MedicationSchedule getSelectedMedication() {
        MedicationSchedule selected = scheduledTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            selected = pendingTable.getSelectionModel().getSelectedItem();
        }
        return selected;
    }
    
    private void clearFields() {
        dosageGivenField.clear();
        notesTextArea.clear();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Medication Administration");
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
    
    public void setCurrentNurse(Staff nurse) {
        this.currentNurse = nurse;
        System.out.println("MedicationAdministrationController: Set current nurse to " + (nurse != null ? nurse.getFullName() : "null"));
    }
}
