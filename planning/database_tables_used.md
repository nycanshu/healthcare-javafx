# Database Tables Used in Simplified Medication Administration Page

## 🗄️ **Primary Tables Used:**

### **1. Residents Table**
- **Purpose**: Store patient/resident information
- **Key Fields**: `resident_id`, `first_name`, `last_name`, `current_bed_id`
- **Usage**: Populate patient dropdown with "ID - Name" format
- **Query**: `SELECT * FROM Residents`

### **2. Medicines Table**
- **Purpose**: Store available medications
- **Key Fields**: `medicine_id`, `name`, `description`, `dosage_unit`
- **Usage**: Populate medicine dropdown with "ID - Name" format
- **Query**: `SELECT * FROM Medicines WHERE is_active = true`

### **3. Administered_Medication Table**
- **Purpose**: Record when nurses administer medications
- **Key Fields**: 
  - `admin_id` (Primary Key)
  - `prescription_medicine_id` (Foreign Key)
  - `nurse_id` (Foreign Key)
  - `administered_time`
  - `dosage_given`
  - `notes`
  - `status` (Given/Missed/Refused)
- **Usage**: Store medication administration records
- **Query**: `INSERT INTO Administered_Medication (...) VALUES (...)`

## 🔗 **Related Tables (Referenced):**

### **4. Staff Table**
- **Purpose**: Store nurse information
- **Key Fields**: `staff_id`, `first_name`, `last_name`, `role`
- **Usage**: Track which nurse administered medication
- **Relationship**: `nurse_id` → `Staff.staff_id`

### **5. Prescription_Medicine Table**
- **Purpose**: Link prescriptions to specific medicines
- **Key Fields**: `id`, `prescription_id`, `medicine_id`, `dosage`, `frequency`
- **Usage**: Required for `prescription_medicine_id` in Administered_Medication
- **Note**: Currently using mock ID (1L) for demonstration

## 📊 **Data Flow in Simplified Medication Page:**

```
1. Load Patients → Residents Table → Display "ID - Name"
2. Load Medicines → Medicines Table → Display "ID - Name"  
3. Administer Medication → Administered_Medication Table → Record administration
4. View Today's Schedule → Administered_Medication Table → Show status
```

## 🎯 **Key Database Operations:**

### **Read Operations:**
```sql
-- Load all patients
SELECT resident_id, first_name, last_name FROM Residents;

-- Load all active medicines  
SELECT medicine_id, name FROM Medicines WHERE is_active = true;

-- Get today's administrations
SELECT * FROM Administered_Medication 
WHERE nurse_id = ? AND DATE(administered_time) = CURDATE();
```

### **Write Operations:**
```sql
-- Record medication administration
INSERT INTO Administered_Medication 
(prescription_medicine_id, nurse_id, administered_time, dosage_given, notes, status, created_at)
VALUES (?, ?, ?, ?, ?, 'Given', NOW());
```

## 🏗️ **Table Relationships:**

```
Residents (1) ←→ (M) Administered_Medication
    ↓
Staff (1) ←→ (M) Administered_Medication  
    ↓
Prescription_Medicine (1) ←→ (M) Administered_Medication
    ↓
Medicines (1) ←→ (M) Prescription_Medicine
```

## 📈 **Summary Statistics Tables:**

The page also calculates statistics from these tables:
- **Total Patients**: Count from Residents table
- **Medications Due**: Count from Prescription_Medicine table
- **Completed**: Count from Administered_Medication with status='Given'
- **Overdue**: Count from Prescription_Medicine (time-based logic)

## 🔧 **Current Implementation Notes:**

1. **Mock Data**: Currently uses sample data for medication schedule
2. **Prescription System**: Simplified - uses mock prescription_medicine_id
3. **Real-time Updates**: Statistics update when data changes
4. **Error Handling**: Database errors are caught and displayed to user

## 🚀 **Future Enhancements:**

1. **Real Prescription Integration**: Connect to actual prescription system
2. **Time-based Scheduling**: Implement proper medication scheduling
3. **Nurse Assignment**: Filter patients by nurse assignment
4. **Audit Trail**: Enhanced logging of all medication activities
