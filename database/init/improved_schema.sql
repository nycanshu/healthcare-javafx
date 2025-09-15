-- =====================================================
-- IMPROVED HEALTHCARE DATABASE SCHEMA
-- Based on Instruction (1).txt requirements
-- =====================================================

-- Wards Table (Ward 1 & Ward 2 as specified)
CREATE TABLE Wards (
    ward_id INT PRIMARY KEY AUTO_INCREMENT,
    ward_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rooms Table (6 rooms per ward as specified)
CREATE TABLE Rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    ward_id INT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    room_type ENUM('Standard', 'Isolation', 'Special') DEFAULT 'Standard',
    max_capacity INT DEFAULT 4, -- 1-4 beds per room as specified
    gender_preference ENUM('Male', 'Female', 'Mixed') DEFAULT 'Mixed',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ward_id) REFERENCES Wards(ward_id),
    UNIQUE KEY unique_room_per_ward (ward_id, room_number)
);

-- Beds Table (Improved with better structure)
CREATE TABLE Beds (
    bed_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    bed_number VARCHAR(20) NOT NULL,
    bed_type ENUM('Standard', 'Electric', 'Special') DEFAULT 'Standard',
    is_occupied BOOLEAN DEFAULT FALSE,
    occupied_by INT NULL,
    gender_restriction ENUM('Male', 'Female', 'None') DEFAULT 'None',
    isolation_required BOOLEAN DEFAULT FALSE,
    last_cleaned TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id),
    FOREIGN KEY (occupied_by) REFERENCES Residents(resident_id),
    UNIQUE KEY unique_bed_per_room (room_id, bed_number)
);

-- Residents Table (Enhanced)
CREATE TABLE Residents (
    resident_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender ENUM('M', 'F') NOT NULL,
    birth_date DATE,
    admission_date DATE NOT NULL,
    discharge_date DATE NULL,
    current_bed_id INT NULL,
    medical_condition TEXT,
    requires_isolation BOOLEAN DEFAULT FALSE,
    emergency_contact VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (current_bed_id) REFERENCES Beds(bed_id)
);

-- Staff Table (Enhanced with shift management)
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Manager', 'Doctor', 'Nurse') NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Shift Schedule Table (For compliance requirements)
CREATE TABLE Shift_Schedule (
    shift_id INT PRIMARY KEY AUTO_INCREMENT,
    staff_id INT NOT NULL,
    shift_date DATE NOT NULL,
    shift_type ENUM('Morning', 'Afternoon', 'Doctor') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    ward_id INT,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id),
    FOREIGN KEY (ward_id) REFERENCES Wards(ward_id)
);

-- Prescriptions Table (Enhanced)
CREATE TABLE Prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    resident_id INT NOT NULL,
    doctor_id INT NOT NULL,
    prescription_date DATE NOT NULL,
    notes TEXT,
    status ENUM('Active', 'Completed', 'Cancelled') DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id),
    FOREIGN KEY (doctor_id) REFERENCES Staff(staff_id)
);

-- Medicines Table (Enhanced)
CREATE TABLE Medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    dosage_unit VARCHAR(20) DEFAULT 'mg',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Prescription Medicines Table (Enhanced)
CREATE TABLE Prescription_Medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,
    medicine_id INT NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    frequency VARCHAR(100) NOT NULL, -- e.g., "Every 8 hours", "Twice daily"
    start_date DATE NOT NULL,
    end_date DATE,
    instructions TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES Prescriptions(prescription_id),
    FOREIGN KEY (medicine_id) REFERENCES Medicines(medicine_id)
);

-- Administered Medication Table (Enhanced)
CREATE TABLE Administered_Medication (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_medicine_id INT NOT NULL,
    nurse_id INT NOT NULL,
    administered_time DATETIME NOT NULL,
    dosage_given VARCHAR(50),
    notes TEXT,
    status ENUM('Given', 'Missed', 'Refused') DEFAULT 'Given',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_medicine_id) REFERENCES Prescription_Medicines(id),
    FOREIGN KEY (nurse_id) REFERENCES Staff(staff_id)
);

-- Actions Log Table (Enhanced for auditing)
CREATE TABLE Actions_Log (
    action_id INT PRIMARY KEY AUTO_INCREMENT,
    staff_id INT,
    action_type ENUM('Admit', 'Discharge', 'Transfer', 'Prescribe', 'Administer', 'Update') NOT NULL,
    action_description VARCHAR(200) NOT NULL,
    resident_id INT,
    bed_id INT,
    action_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    ip_address VARCHAR(45),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id),
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id),
    FOREIGN KEY (bed_id) REFERENCES Beds(bed_id)
);

-- Archive Table (Enhanced)
CREATE TABLE Archive (
    archive_id INT PRIMARY KEY AUTO_INCREMENT,
    resident_id INT NOT NULL,
    discharge_date DATE NOT NULL,
    total_stay_days INT,
    archived_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    archive_reason VARCHAR(100),
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id)
);

-- =====================================================
-- SAMPLE DATA INSERTION
-- =====================================================

-- Insert Wards
INSERT INTO Wards (ward_name, description) VALUES 
('Ward 1', 'General care ward'),
('Ward 2', 'Specialized care ward');

-- Insert Rooms (6 rooms per ward as specified)
INSERT INTO Rooms (ward_id, room_number, room_type, max_capacity, gender_preference) VALUES 
-- Ward 1 Rooms
(1, '101', 'Standard', 4, 'Mixed'),
(1, '102', 'Standard', 3, 'Mixed'),
(1, '103', 'Isolation', 1, 'Mixed'),
(1, '104', 'Standard', 4, 'Mixed'),
(1, '105', 'Standard', 2, 'Mixed'),
(1, '106', 'Special', 2, 'Mixed'),

-- Ward 2 Rooms
(2, '201', 'Standard', 4, 'Mixed'),
(2, '202', 'Standard', 3, 'Mixed'),
(2, '203', 'Isolation', 1, 'Mixed'),
(2, '204', 'Standard', 4, 'Mixed'),
(2, '205', 'Standard', 2, 'Mixed'),
(2, '206', 'Special', 2, 'Mixed');

-- Insert Beds (1-4 beds per room as specified)
INSERT INTO Beds (room_id, bed_number, bed_type, gender_restriction) VALUES 
-- Room 101 (4 beds)
(1, 'A', 'Standard', 'None'),
(1, 'B', 'Standard', 'None'),
(1, 'C', 'Standard', 'None'),
(1, 'D', 'Standard', 'None'),

-- Room 102 (3 beds)
(2, 'A', 'Standard', 'None'),
(2, 'B', 'Standard', 'None'),
(2, 'C', 'Standard', 'None'),

-- Room 103 (1 bed - Isolation)
(3, 'A', 'Special', 'None'),

-- Room 104 (4 beds)
(4, 'A', 'Standard', 'None'),
(4, 'B', 'Standard', 'None'),
(4, 'C', 'Standard', 'None'),
(4, 'D', 'Standard', 'None'),

-- Room 105 (2 beds)
(5, 'A', 'Standard', 'None'),
(5, 'B', 'Standard', 'None'),

-- Room 106 (2 beds - Special)
(6, 'A', 'Electric', 'None'),
(6, 'B', 'Electric', 'None'),

-- Ward 2 beds (similar pattern)
(7, 'A', 'Standard', 'None'),
(7, 'B', 'Standard', 'None'),
(7, 'C', 'Standard', 'None'),
(7, 'D', 'Standard', 'None'),

(8, 'A', 'Standard', 'None'),
(8, 'B', 'Standard', 'None'),
(8, 'C', 'Standard', 'None'),

(9, 'A', 'Special', 'None'),

(10, 'A', 'Standard', 'None'),
(10, 'B', 'Standard', 'None'),
(10, 'C', 'Standard', 'None'),
(10, 'D', 'Standard', 'None'),

(11, 'A', 'Standard', 'None'),
(11, 'B', 'Standard', 'None'),

(12, 'A', 'Electric', 'None'),
(12, 'B', 'Electric', 'None');

-- Insert Sample Staff
INSERT INTO Staff (username, password, role, first_name, last_name) VALUES 
('manager', 'password', 'Manager', 'John', 'Manager'),
('doctor1', 'password', 'Doctor', 'Dr. Sarah', 'Smith'),
('nurse1', 'password', 'Nurse', 'Mary', 'Johnson'),
('nurse2', 'password', 'Nurse', 'Lisa', 'Brown');

-- Insert Sample Medicines
INSERT INTO Medicines (name, description, dosage_unit) VALUES 
('Paracetamol', 'Pain relief and fever reducer', 'mg'),
('Ibuprofen', 'Anti-inflammatory pain relief', 'mg'),
('Insulin', 'Blood sugar management', 'units'),
('Morphine', 'Strong pain relief', 'mg');
