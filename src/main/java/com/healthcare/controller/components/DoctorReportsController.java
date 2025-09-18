package com.healthcare.controller.components;

import com.healthcare.model.Prescription;
import com.healthcare.model.Resident;
import com.healthcare.model.Staff;
import com.healthcare.services.PrescriptionService;
import com.healthcare.services.ResidentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Component Controller for Doctor Reports
 * Handles report generation for doctors
 */
public class DoctorReportsController implements Initializable {
    
    // FXML Elements
    @FXML private Label totalPatientsLabel;
    @FXML private Label totalPrescriptionsLabel;
    @FXML private Label thisMonthLabel;
    @FXML private ListView<String> reportsListView;
    
    // Data
    private ObservableList<String> reportsList = FXCollections.observableArrayList();
    private Staff currentDoctor;
    
    // Services
    private PrescriptionService prescriptionService = new PrescriptionService();
    private ResidentService residentService = new ResidentService();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupReports();
        loadData();
    }
    
    /**
     * Set the current doctor for report generation
     */
    public void setCurrentDoctor(Staff doctor) {
        this.currentDoctor = doctor;
        loadData();
    }
    
    private void setupReports() {
        System.out.println("Setting up doctor reports component...");
        
        // Initialize reports list
        reportsList.add("📊 Patient Summary Report");
        reportsList.add("💊 Prescription History Report");
        reportsList.add("📅 Monthly Activity Report");
        reportsList.add("👥 Patient Assignment Report");
        reportsList.add("📈 Performance Metrics Report");
        
        reportsListView.setItems(reportsList);
    }
    
    private void loadData() {
        try {
            if (currentDoctor == null) {
                System.out.println("No current doctor set, cannot load report data");
                return;
            }
            
            // Load patient count
            List<Resident> residents = residentService.findAll();
            long patientCount = residents.stream()
                .filter(resident -> resident.getAssignedDoctorId() != null && 
                       resident.getAssignedDoctorId().equals(currentDoctor.getStaffId()))
                .count();
            totalPatientsLabel.setText(String.valueOf(patientCount));
            
            // Load prescription count
            List<Prescription> prescriptions = prescriptionService.findAll();
            long prescriptionCount = prescriptions.stream()
                .filter(prescription -> prescription.getDoctorId().equals(currentDoctor.getStaffId()))
                .count();
            totalPrescriptionsLabel.setText(String.valueOf(prescriptionCount));
            
            // Load this month's count
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            long thisMonthCount = prescriptions.stream()
                .filter(prescription -> prescription.getDoctorId().equals(currentDoctor.getStaffId()) &&
                       prescription.getPrescriptionDate().isAfter(startOfMonth.minusDays(1)))
                .count();
            thisMonthLabel.setText(String.valueOf(thisMonthCount));
            
            System.out.println("Doctor reports component loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Error loading report data: " + e.getMessage());
            showError("Failed to load report data");
        }
    }
    
    // Action methods
    @FXML
    private void generatePatientReport() {
        try {
            if (currentDoctor == null) {
                showError("No doctor selected");
                return;
            }
            
            List<Resident> residents = residentService.findAll();
            List<Resident> myPatients = residents.stream()
                .filter(resident -> resident.getAssignedDoctorId() != null && 
                       resident.getAssignedDoctorId().equals(currentDoctor.getStaffId()))
                .collect(java.util.stream.Collectors.toList());
            
            StringBuilder report = new StringBuilder();
            report.append("PATIENT REPORT FOR DR. ").append(currentDoctor.getFullName()).append("\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(50)).append("\n\n");
            
            if (myPatients.isEmpty()) {
                report.append("No patients assigned to this doctor.\n");
            } else {
                report.append("Total Patients: ").append(myPatients.size()).append("\n\n");
                report.append("Patient Details:\n");
                report.append("-".repeat(30)).append("\n");
                
                for (Resident patient : myPatients) {
                    report.append("Name: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
                    report.append("Gender: ").append(patient.getGender()).append("\n");
                    report.append("Admission Date: ").append(patient.getAdmissionDate()).append("\n");
                    report.append("Medical Condition: ").append(patient.getMedicalCondition() != null ? patient.getMedicalCondition() : "Not specified").append("\n");
                    report.append("-".repeat(30)).append("\n");
                }
            }
            
            showReport("Patient Report", report.toString());
            
        } catch (Exception e) {
            System.err.println("Error generating patient report: " + e.getMessage());
            showError("Failed to generate patient report: " + e.getMessage());
        }
    }
    
    @FXML
    private void generatePrescriptionReport() {
        try {
            if (currentDoctor == null) {
                showError("No doctor selected");
                return;
            }
            
            List<Prescription> prescriptions = prescriptionService.findAll();
            List<Prescription> myPrescriptions = prescriptions.stream()
                .filter(prescription -> prescription.getDoctorId().equals(currentDoctor.getStaffId()))
                .collect(java.util.stream.Collectors.toList());
            
            StringBuilder report = new StringBuilder();
            report.append("PRESCRIPTION REPORT FOR DR. ").append(currentDoctor.getFullName()).append("\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(50)).append("\n\n");
            
            if (myPrescriptions.isEmpty()) {
                report.append("No prescriptions found for this doctor.\n");
            } else {
                report.append("Total Prescriptions: ").append(myPrescriptions.size()).append("\n\n");
                
                long activeCount = myPrescriptions.stream()
                    .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.Active)
                    .count();
                long completedCount = myPrescriptions.stream()
                    .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.Completed)
                    .count();
                long pendingCount = myPrescriptions.stream()
                    .filter(p -> p.getReviewStatus() == Prescription.ReviewStatus.Pending)
                    .count();
                
                report.append("Active Prescriptions: ").append(activeCount).append("\n");
                report.append("Completed Prescriptions: ").append(completedCount).append("\n");
                report.append("Pending Reviews: ").append(pendingCount).append("\n\n");
                
                report.append("Recent Prescriptions:\n");
                report.append("-".repeat(40)).append("\n");
                
                myPrescriptions.stream()
                    .sorted((p1, p2) -> p2.getPrescriptionDate().compareTo(p1.getPrescriptionDate()))
                    .limit(10)
                    .forEach(prescription -> {
                        report.append("Date: ").append(prescription.getPrescriptionDate()).append("\n");
                        report.append("Status: ").append(prescription.getStatus()).append("\n");
                        report.append("Review Status: ").append(prescription.getReviewStatus()).append("\n");
                        report.append("Notes: ").append(prescription.getNotes() != null ? prescription.getNotes() : "None").append("\n");
                        report.append("-".repeat(40)).append("\n");
                    });
            }
            
            showReport("Prescription Report", report.toString());
            
        } catch (Exception e) {
            System.err.println("Error generating prescription report: " + e.getMessage());
            showError("Failed to generate prescription report: " + e.getMessage());
        }
    }
    
    @FXML
    private void generateMonthlyReport() {
        try {
            if (currentDoctor == null) {
                showError("No doctor selected");
                return;
            }
            
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            
            List<Prescription> prescriptions = prescriptionService.findAll();
            List<Prescription> monthlyPrescriptions = prescriptions.stream()
                .filter(prescription -> prescription.getDoctorId().equals(currentDoctor.getStaffId()) &&
                       prescription.getPrescriptionDate().isAfter(startOfMonth.minusDays(1)) &&
                       prescription.getPrescriptionDate().isBefore(endOfMonth.plusDays(1)))
                .collect(java.util.stream.Collectors.toList());
            
            StringBuilder report = new StringBuilder();
            report.append("MONTHLY REPORT FOR DR. ").append(currentDoctor.getFullName()).append("\n");
            report.append("Month: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n");
            report.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
            report.append("=".repeat(50)).append("\n\n");
            
            report.append("Monthly Statistics:\n");
            report.append("-".repeat(20)).append("\n");
            report.append("Total Prescriptions: ").append(monthlyPrescriptions.size()).append("\n");
            
            long activeCount = monthlyPrescriptions.stream()
                .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.Active)
                .count();
            long completedCount = monthlyPrescriptions.stream()
                .filter(p -> p.getStatus() == Prescription.PrescriptionStatus.Completed)
                .count();
            
            report.append("Active: ").append(activeCount).append("\n");
            report.append("Completed: ").append(completedCount).append("\n");
            
            if (!monthlyPrescriptions.isEmpty()) {
                report.append("\nDaily Breakdown:\n");
                report.append("-".repeat(20)).append("\n");
                
                monthlyPrescriptions.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                        Prescription::getPrescriptionDate,
                        java.util.stream.Collectors.counting()
                    ))
                    .entrySet()
                    .stream()
                    .sorted(java.util.Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        report.append(entry.getKey()).append(": ").append(entry.getValue()).append(" prescriptions\n");
                    });
            }
            
            showReport("Monthly Report", report.toString());
            
        } catch (Exception e) {
            System.err.println("Error generating monthly report: " + e.getMessage());
            showError("Failed to generate monthly report: " + e.getMessage());
        }
    }
    
    @FXML
    private void refreshReports() {
        loadData();
        showSuccess("Reports refreshed successfully");
    }
    
    private void showReport(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefSize(600, 400);
        
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20);
        
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
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
