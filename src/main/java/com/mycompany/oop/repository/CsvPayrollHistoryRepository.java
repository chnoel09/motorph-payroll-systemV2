/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.PayrollHistoryRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
    CsvPayrollHistoryRepository

    Handles persistence of payroll history records
    using a CSV file as storage.

    Responsibilities:
    - Save payroll records
    - Load all records
    - Filter by employee
    - Check cutoff duplicates
    - Delete by cutoff
*/

public class CsvPayrollHistoryRepository implements PayrollHistoryRepository {

    private static final String FILE_PATH = "src/main/resources/payroll_history.csv";

    @Override
    public void savePayrollRecord(PayrollHistoryRecord record) {

        try {
            File file = new File(FILE_PATH);
            boolean needsNewLine = file.exists() && file.length() > 0;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                if (needsNewLine) {
                    writer.newLine();
                }
                writer.write(formatRecord(record));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PayrollHistoryRecord> findAll() {

        List<PayrollHistoryRecord> records = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length < 11) {
                    System.out.println("Skipped invalid payroll history row: " + line);
                    continue;
                }

                try {
                    PayrollHistoryRecord record = new PayrollHistoryRecord(
                            Integer.parseInt(data[0].trim()),
                            data[1].trim(),
                            Double.parseDouble(data[2].trim()),
                            Double.parseDouble(data[3].trim()),
                            Double.parseDouble(data[4].trim()),
                            Double.parseDouble(data[5].trim()),
                            Double.parseDouble(data[6].trim()),
                            Double.parseDouble(data[7].trim()),
                            Double.parseDouble(data[8].trim()),
                            Double.parseDouble(data[9].trim()),
                            Double.parseDouble(data[10].trim())
                    );

                    records.add(record);

                } catch (NumberFormatException ex) {
                    System.out.println("Skipped malformed payroll history row: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public List<PayrollHistoryRecord> findByEmployeeId(int employeeId) {

        List<PayrollHistoryRecord> filtered = new ArrayList<>();

        for (PayrollHistoryRecord record : findAll()) {
            if (record.getEmployeeId() == employeeId) {
                filtered.add(record);
            }
        }

        return filtered;
    }

    @Override
    public boolean existsByCutoff(String cutoffPeriod) {

        for (PayrollHistoryRecord record : findAll()) {
            if (record.getCutoffPeriod().equalsIgnoreCase(cutoffPeriod)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void deleteByCutoff(String cutoffPeriod) {

        List<PayrollHistoryRecord> updated = new ArrayList<>();

        for (PayrollHistoryRecord record : findAll()) {
            if (!record.getCutoffPeriod().equalsIgnoreCase(cutoffPeriod)) {
                updated.add(record);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (int i = 0; i < updated.size(); i++) {
                writer.write(formatRecord(updated.get(i)));
                if (i < updated.size() - 1) {
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatRecord(PayrollHistoryRecord record) {
        return record.getEmployeeId() + "," +
                record.getCutoffPeriod() + "," +
                record.getBasicComponent() + "," +
                record.getAllowanceComponent() + "," +
                record.getGross() + "," +
                record.getSss() + "," +
                record.getPhilhealth() + "," +
                record.getPagibig() + "," +
                record.getTax() + "," +
                record.getTotalDeductions() + "," +
                record.getNet();
    }
}

/*
CSV PAYROLL HISTORY REPOSITORY – IMPLEMENTATION SUMMARY

Purpose:
Concrete implementation of PayrollHistoryRepository using CSV file storage.

Key Responsibilities:
• Save payroll history records
• Read all payroll history records
• Filter payroll history by employee
• Detect duplicate cutoff processing
• Delete all records for a selected cutoff

Important Fixes:
• Standardized save and delete operations to use the same full CSV structure
• Added safe file existence check
• Added safe CSV row validation
• Added malformed row protection during parsing
• Centralized row formatting through formatRecord()

CSV Structure:
employeeId,cutoffPeriod,basicComponent,allowanceComponent,gross,sss,philhealth,pagibig,tax,totalDeductions,net

Duplicate Prevention:
existsByCutoff() scans saved history and prevents processing
the same cutoff more than once unless overwrite/reset is performed.

Clear Cutoff Behavior:
deleteByCutoff() rewrites payroll_history.csv excluding all rows
that belong to the selected cutoff period.

Design Pattern:
Implements Repository Pattern.
PayrollService depends on the repository abstraction,
not directly on file operations.

Scalability:
This repository can later be replaced by a database-backed implementation
without changing PayrollService logic.
*/