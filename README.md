# MotorPH Payroll System  
MO-IT113 - Advanced Object-oriented Programming &
MO-IT111 - Database Principles and Applications (Final Implementation)
Term 3  SY 2025 - 26

---

## Project Overview

The MotorPH Payroll System is a Java-based payroll management application developed for MO-IT113 - Advanced Object-oriented Programming and MO-IT111 - Database Principles and Applications.

This system is the finalized implementation of the MotorPH Payroll System, transitioning from file-based storage to a structured database-driven system using MySQL.

This version focuses on:

- Implementing a layered architecture  
- Integrating database persistence using MySQL  
- Enforcing separation of concerns  
- Centralizing payroll computation logic  
- Ensuring data integrity through constraints and relationships  
- Improving system reliability and usability  

This repository contains the finalized and fully functional implementation.

---

## System Architecture

The application follows a layered architecture:

View Layer → Service Layer → Repository Layer → Database → Model

---

### 1. View Layer

Responsible for user interaction and display only.

Includes:

- LoginFrame  
- MainAppFrame  
- DashboardPanel  
- EmployeePanel  
- PayrollPanel  
- AttendancePanel  
- LeavePanel  

The UI contains no business logic.

---

### 2. Service Layer

Handles application coordination and business rule enforcement.

Classes:

- PayrollService  
- EmployeeService  
- AttendanceService  
- LeaveService  
- LoginService  

Responsibilities:

- Coordinate operations  
- Enforce business validation rules  
- Manage role-based access control  
- Process system workflows  

---

### 3. Repository Layer

Repositories abstract database operations using JDBC.

Repositories:

- EmployeeRepository  
- AttendanceRepository  
- PayrollRepository  
- LeaveRepository  

Responsibilities:

- Execute SQL queries  
- Retrieve and persist data  
- Abstract database interaction from business logic  

---

### 4. Database Layer

The system uses a MySQL database for data persistence.

Tables:

- employees  
- attendance_records  
- payroll_history  
- leaves  

Key features:

- Primary Keys and Foreign Keys  
- Unique constraints (email, username)  
- NOT NULL constraints  
- Data validation rules  

Database structure is provided via:
/database/motorph_db.sql

---

### 5. Model Classes

- Employee  
- PayrollRecord  
- AttendanceRecord  
- LeaveRequest  

These classes encapsulate structured system data.

---

## Payroll Computation Logic

Payroll follows structured cutoff computation:

1. Gross Pay (Cutoff)  
   Gross = Basic (Semi-Monthly) + Allowance (Semi-Monthly)

2. Monthly Equivalent  
   Monthly Equivalent = Gross × 2  

3. Monthly Deductions  

   - SSS  
   - PhilHealth  
   - Pag-IBIG  
   - Withholding Tax  

4. Cutoff Deductions  
   Cutoff Deduction = Monthly Deduction ÷ 2  

5. Net Pay  
   Net = Gross − Total Cutoff Deductions  

All payroll calculations are centralized within the system logic.

---

## Database Integration

All system records are stored in MySQL.

The database ensures:

- Persistent storage  
- Data consistency  
- Relational integrity  
- Prevention of invalid or duplicate entries  

The database can be initialized using:
/database/motorph_db.sql

---

## Features

- Authentication (Login System)  
- Role-Based Access Control (RBAC)  
- Employee Management  
- Attendance Tracking  
- Payroll Processing  
- Payroll History Tracking  
- Leave Management  
- Dashboard Metrics  

---

## How to Run the System

1. Install MySQL  
2. Import the database:
/database/motorph_db.sql

3. Update database credentials in:
DatabaseConnection.java

4. Run the application starting from:
LoginFrame.java

---

## System Behavior Notes

- Payroll depends on attendance records  
- If no attendance is recorded, payroll will not be available  
- Hours worked are computed from time in/out  
- Payroll values update dynamically based on attendance  
- All data is stored and retrieved from the database  

---

## Group Information

Group #
Section S2101  

Members:

- Rosephil Muros  
- Claire Helery Noel  

---

## Final Notes

This submission represents the finalized MotorPH Payroll System integrating AOOP with DPA.

The system demonstrates:

- Clean layered architecture  
- Proper separation of concerns  
- Reliable data storage  
- Functional and stable system behavior  

All required functionalities are implemented and validated for evaluation.
