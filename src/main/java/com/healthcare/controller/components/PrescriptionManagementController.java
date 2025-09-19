package com.healthcare.controller.components;

import com.healthcare.model.Prescription;
import com.healthcare.model.Resident;
import com.healthcare.model.Medicine;
import com.healthcare.model.Staff;
import com.healthcare.services.PrescriptionService;
import com.healthcare.services.ResidentService;
import com.healthcare.services.MedicineService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Component Controller for Prescription Management
 * Handles prescription CRUD operations for doctors
 */
public class PrescriptionManagementController implements Initializable {
    
    // FXML Elements
    @FXML private Label activePrescriptionsLabel;
    @FXML private Label pendingReviewsLabel;
    @FXML private Label completedTodayLabel;
    @FXML private TableView<Prescription> prescriptionsTable;
    @FXML private TableColumn<Prescription, String> patientNameColumn;
    @FXML private TableColumn<Prescription, LocalDate> prescriptionDateColumn;
    @FXML private TableColumn<Prescription, String> medicinesColumn;
    @FXML private TableColumn<Prescription, String> statusColumn;
    @FXML private TableColumn<Prescription, String> reviewStatusColumn;
    @FXML private TableColumn<Prescription, Void> actionsColumn;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private TextField searchField;
    
    // Form Elements
    @FXML private VBox prescriptionFormContainer;
    @FXML private Label formTitleLabel;
    @FXML private ComboBox<Resident> patientComboBox;
    @FXML private DatePicker prescriptionDatePicker;
    @FXML private ComboBox<Medicine> medicineComboBox;
    @FXML private TextField dosageField;
    @FXML private TextField frequencyField;
    @FXML private ListView<String> medicinesListView;
    @FXML private TextArea notesTextArea;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    
    // Data
    private ObservableList<Prescription> prescriptionsList = FXCollections.observableArrayList();
    private ObservableList<String> medicinesList = FXCollections.observableArrayList();
    private Staff currentDoctor;
    
    // Services
    private PrescriptionService prescriptionService = new PrescriptionService();
    private ResidentService residentService = new ResidentService();
    private MedicineService medicineService = new MedicineService();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupForm();
        loadData();
    }
    
    /**
     * Set the current doctor for prescription management
     */
    public void setCurrentDoctor(Staff doctor) {
        this.currentDoctor = doctor;
        loadData();
    }
    
    private void setupTable() {
        System.out.println("Setting up prescription management component...");
        
        // Setup table columns
        patientNameColumn.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            // Get patient name from resident ID
            try {
                List<Resident> residents = residentService.findAll();
                Resident patient = residents.stream()
                    .filter(r -> r.getResidentId().equals(prescription.getResidentId()))
                    .findFirst()
                    .orElse(null);
                return new javafx.beans.property.SimpleStringProperty(
                    patient != null ? patient.getFirstName() + " " + patient.getLastName() : "Unknown"
                );
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty("Unknown");
            }
        });
        
        prescriptionDateColumn.setCellValueFactory(new PropertyValueFactory<>("prescriptionDate"));
        
        medicinesColumn.setCellValueFactory(cellData -> {
            // TODO: Get medicines from prescription
            return new javafx.beans.property.SimpleStringProperty("Multiple medicines");
        });
        
        statusColumn.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                prescription.getStatus() != null ? prescription.getStatus().toString() : "Unknown"
            );
        });
        
        reviewStatusColumn.setCellValueFactory(cellData -> {
            Prescription prescription = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                prescription.getReviewStatus() != null ? prescription.getReviewStatus().toString() : "Unknown"
            );
        });
        
        // Setup actions column
        actionsColumn.setCellFactory(col -> new TableCell<Prescription, Void>() {
            private final Button viewButton = new Button("👁️ View");
            private final Button editButton = new Button("✏️ Edit");
            private final Button deleteButton = new Button("🗑️ Delete");
            
            {
                viewButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                editButton.setStyle("-fx-background-color: #F18F01; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                deleteButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-background-radius: 4; -fx-padding: 4 8;");
                
                viewButton.setOnAction(e -> viewPrescription(getTableView().getItems().get(getIndex())));
                editButton.setOnAction(e -> editPrescription(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(e -> deletePrescription(getTableView().getItems().get(getIndex())));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(5, viewButton, editButton, deleteButton));
                }
            }
        });
        
        // Setup search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterPrescriptions());
        
        // Setup status filter
        statusFilterComboBox.setItems(FXCollections.observableArrayList(
            "All", "Active", "Completed", "Cancelled", "Pending", "Reviewed", "Approved", "Rejected"
        ));
        statusFilterComboBox.setValue("All");
        statusFilterComboBox.setOnAction(e -> filterPrescriptions());
    }
    
    private void setupForm() {
        // Setup date picker
        prescriptionDatePicker.setValue(LocalDate.now());
        
        // Setup medicine combo box
        try {
            List<Medicine> medicines = medicineService.findAll();
            medicineComboBox.setItems(FXCollections.observableArrayList(medicines));
            medicineComboBox.setCellFactory(listView -> new ListCell<Medicine>() {
                @Override
                protected void updateItem(Medicine medicine, boolean empty) {
                    super.updateItem(medicine, empty);
                    if (empty || medicine == null) {
                        setText(null);
                    } else {
                        setText(medicine.getName() + " (" + medicine.getDosageUnit() + ")");
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading medicines: " + e.getMessage());
        }
        
        // Setup patient combo box
        try {
            List<Resident> residents = residentService.findAll();
            patientComboBox.setItems(FXCollections.observableArrayList(residents));
            patientComboBox.setCellFactory(listView -> new ListCell<Resident>() {
                @Override
                protected void updateItem(Resident resident, boolean empty) {
                    super.updateItem(resident, empty);
                    if (empty || resident == null) {
                        setText(null);
                    } else {
                        setText(resident.getFirstName() + " " + resident.getLastName());
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error loading patients: " + e.getMessage());
        }
    }
    
    private void loadData() {
        try {
            List<Prescription> prescriptions = prescriptionService.findAll();
            prescriptionsList.setAll(prescriptions);
            prescriptionsTable.setItems(prescriptionsList);
            
            // Update statistics
            long activeCount = prescriptions.stream()
                .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.Active)
                .count();
            long pendingCount = prescriptions.stream()
                .filter(p -> p.getReviewStatus() == Prescription.ReviewStatus.Pending)
                .count();
            long completedToday = prescriptions.stream()
                .filter(p -> p.getPrescriptionDate().equals(LocalDate.now()) && 
                           p.getStatus() == Prescription.PrescriptionStatus.Completed)
                .count();
            
            activePrescriptionsLabel.setText(String.valueOf(activeCount));
            pendingReviewsLabel.setText(String.valueOf(pendingCount));
            completedTodayLabel.setText(String.valueOf(completedToday));
            
            System.out.println("Prescription management component loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Error loading prescription data: " + e.getMessage());
            showError("Failed to load prescription data");
        }
    }
    
    private void filterPrescriptions() {
        String searchText = searchField.getText().toLowerCase();
        String statusFilter = statusFilterComboBox.getValue();
        
        ObservableList<Prescription> filteredList = FXCollections.observableArrayList();
        
        for (Prescription prescription : prescriptionsList) {
            // Search filter
            boolean matchesSearch = searchText.isEmpty() || 
                (prescription.getNotes() != null && prescription.getNotes().toLowerCase().contains(searchText));
            
            // Status filter
            boolean matchesStatus = statusFilter.equals("All") ||
                (statusFilter.equals("Active") && prescription.getStatus() == Prescription.PrescriptionStatus.Active) ||
                (statusFilter.equals("Completed") && prescription.getStatus() == Prescription.PrescriptionStatus.Completed) ||
                (statusFilter.equals("Cancelled") && prescription.getStatus() == Prescription.PrescriptionStatus.Cancelled) ||
                (statusFilter.equals("Pending") && prescription.getReviewStatus() == Prescription.ReviewStatus.Pending) ||
                (statusFilter.equals("Reviewed") && prescription.getReviewStatus() == Prescription.ReviewStatus.Reviewed) ||
                (statusFilter.equals("Approved") && prescription.getReviewStatus() == Prescription.ReviewStatus.Approved) ||
                (statusFilter.equals("Rejected") && prescription.getReviewStatus() == Prescription.ReviewStatus.Rejected);
            
            if (matchesSearch && matchesStatus) {
                filteredList.add(prescription);
            }
        }
        
        prescriptionsTable.setItems(filteredList);
    }
    
    // Action methods
    @FXML
    private void showNewPrescriptionForm() {
        formTitleLabel.setText("New Prescription");
        prescriptionFormContainer.setVisible(true);
        prescriptionFormContainer.setManaged(true);
        clearForm();
    }
    
    @FXML
    private void refreshPrescriptionsList() {
        loadData();
        showSuccess("Prescription list refreshed");
    }
    
    @FXML
    private void showPendingReviews() {
        statusFilterComboBox.setValue("Pending");
        filterPrescriptions();
    }
    
    @FXML
    private void addMedicineToList() {
        Medicine selectedMedicine = medicineComboBox.getValue();
        String dosage = dosageField.getText().trim();
        String frequency = frequencyField.getText().trim();
        
        if (selectedMedicine == null || dosage.isEmpty() || frequency.isEmpty()) {
            showError("Please select a medicine and enter dosage and frequency");
            return;
        }
        
        String medicineEntry = selectedMedicine.getName() + " - " + dosage + " - " + frequency;
        medicinesList.add(medicineEntry);
        medicinesListView.setItems(medicinesList);
        
        // Clear fields
        medicineComboBox.setValue(null);
        dosageField.clear();
        frequencyField.clear();
    }
    
    @FXML
    private void savePrescription() {
        Resident selectedPatient = patientComboBox.getValue();
        LocalDate prescriptionDate = prescriptionDatePicker.getValue();
        String notes = notesTextArea.getText().trim();
        
        if (selectedPatient == null || prescriptionDate == null) {
            showError("Please select a patient and prescription date");
            return;
        }
        
        if (medicinesList.isEmpty()) {
            showError("Please add at least one medicine");
            return;
        }
        
        try {
            Prescription prescription = new Prescription(
                selectedPatient.getResidentId(),
                currentDoctor.getStaffId(),
                prescriptionDate,
                notes
            );
            
            prescriptionService.save(prescription);
            
            showSuccess("Prescription saved successfully");
            prescriptionFormContainer.setVisible(false);
            prescriptionFormContainer.setManaged(false);
            loadData();
            
        } catch (Exception e) {
            System.err.println("Error saving prescription: " + e.getMessage());
            showError("Failed to save prescription: " + e.getMessage());
        }
    }
    
    @FXML
    private void cancelForm() {
        prescriptionFormContainer.setVisible(false);
        prescriptionFormContainer.setManaged(false);
        clearForm();
    }
    
    private void clearForm() {
        patientComboBox.setValue(null);
        prescriptionDatePicker.setValue(LocalDate.now());
        medicineComboBox.setValue(null);
        dosageField.clear();
        frequencyField.clear();
        medicinesList.clear();
        medicinesListView.setItems(medicinesList);
        notesTextArea.clear();
    }
    
    private void viewPrescription(Prescription prescription) {
        // TODO: Implement view prescription dialog
        showInfo("View prescription: " + prescription.getPrescriptionId());
    }
    
    private void editPrescription(Prescription prescription) {
        // TODO: Implement edit prescription
        showInfo("Edit prescription: " + prescription.getPrescriptionId());
    }
    
    private void deletePrescription(Prescription prescription) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Prescription");
        alert.setContentText("Are you sure you want to delete this prescription?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                prescriptionService.deleteById(prescription.getPrescriptionId());
                showSuccess("Prescription deleted successfully");
                loadData();
            } catch (Exception e) {
                showError("Failed to delete prescription: " + e.getMessage());
            }
        }
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
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
