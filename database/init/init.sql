-- Residents (Patients)
CREATE TABLE Residents (
    resident_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    gender CHAR(1) CHECK (gender IN ('M','F')),
    birth_date DATE,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    current_bed_id INT
);

-- Staff
CREATE TABLE Staff (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Manager','Doctor','Nurse') NOT NULL
);

-- Beds (just link ward/room info directly to keep it simple)
CREATE TABLE Beds (
    bed_id INT PRIMARY KEY AUTO_INCREMENT,
    ward_name VARCHAR(50) NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    bed_number VARCHAR(20) NOT NULL,
    occupied_by INT,
    FOREIGN KEY (occupied_by) REFERENCES Residents(resident_id)
);

-- Prescriptions
CREATE TABLE Prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    resident_id INT NOT NULL,
    doctor_id INT NOT NULL,
    date DATE NOT NULL,
    notes TEXT,
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id),
    FOREIGN KEY (doctor_id) REFERENCES Staff(staff_id)
);

-- Medicines (catalog of available medicines)
CREATE TABLE Medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

-- Prescription details (link prescription to medicines)
CREATE TABLE Prescription_Medicines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,
    medicine_id INT NOT NULL,
    dosage VARCHAR(50),
    schedule VARCHAR(100), -- e.g. "8am, 2pm"
    FOREIGN KEY (prescription_id) REFERENCES Prescriptions(prescription_id),
    FOREIGN KEY (medicine_id) REFERENCES Medicines(medicine_id)
);

-- Medication administration (nurses marking meds given)
CREATE TABLE Administered_Medication (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_medicine_id INT NOT NULL,
    nurse_id INT NOT NULL,
    time DATETIME NOT NULL,
    FOREIGN KEY (prescription_medicine_id) REFERENCES Prescription_Medicines(id),
    FOREIGN KEY (nurse_id) REFERENCES Staff(staff_id)
);

-- Actions log (simple auditing)
CREATE TABLE Actions_Log (
    action_id INT PRIMARY KEY AUTO_INCREMENT,
    staff_id INT,
    action VARCHAR(100),
    time DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT,
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

-- Archive (basic discharged records)
CREATE TABLE Archive (
    archive_id INT PRIMARY KEY AUTO_INCREMENT,
    resident_id INT,
    discharge_date DATE,
    archived_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES Residents(resident_id)
);
