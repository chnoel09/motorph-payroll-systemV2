/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.PayrollHistoryRecord;
import java.util.List;

public interface PayrollHistoryRepository {

    void savePayrollRecord(PayrollHistoryRecord record);

    List<PayrollHistoryRecord> findAll();

    List<PayrollHistoryRecord> findByEmployeeId(int employeeId);

    boolean existsByCutoff(String cutoffPeriod);

    void deleteByCutoff(String cutoffPeriod);
}

/*
PAYROLL HISTORY REPOSITORY – INTERFACE UPDATE SUMMARY

Purpose:
Defines the contract for storing and retrieving payroll history records.

Why It Was Updated:
We expanded payroll functionality to support:
Duplicate cutoff prevention
Employee-specific payslip history
Demo reset capability
Cutoff validation logic

New/Standardized Methods:
savePayrollRecord(...)
Saves a payroll history record to persistent storage.

findAll()
Retrieves all payroll history records.
Used for:
- Duplicate checking
- Cutoff deletion
- Global filtering

findByEmployeeId(int employeeId)
Filters payroll records by employee.
Used by PayslipPanel for employee-specific view.

existsByCutoff(String cutoffPeriod)
Checks if payroll has already been processed for a cutoff.
Prevents duplicate payroll runs.

deleteByCutoff(String cutoffPeriod)
Removes all payroll entries of a specific cutoff.
Used for demo reset or overwrite logic.

Architecture Impact:
This interface enforces separation of concerns between:
Business Logic (PayrollService)
Data Layer (CSV implementation)

Scalability:
Can be replaced by:

Database implementation

REST API repository

Cloud persistence
Without changing service logic.
===============================================================================
*/