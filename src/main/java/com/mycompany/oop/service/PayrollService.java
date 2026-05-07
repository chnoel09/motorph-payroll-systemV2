/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.model.PayrollRecord;
import com.mycompany.oop.model.PayrollSummary;
import com.mycompany.oop.repository.PayrollHistoryDatabaseRepository;
import com.mycompany.oop.repository.PayrollHistoryRepository;

public class PayrollService {

    private EmployeeService employeeService;
    private PayrollProcessor processor;
    private PayrollHistoryRepository historyRepository;
    private AttendanceService attendanceService;

    public PayrollService() {
        this.employeeService = new EmployeeService();
        this.processor = new PayrollProcessor();
        this.historyRepository = new PayrollHistoryDatabaseRepository();
        this.attendanceService = new AttendanceService();
    }

    // ================= SUMMARY (attendance-based) =================

    public PayrollSummary generatePayrollSummary(String cutoffPeriod) {
        List<Employee> employees = employeeService.getAllEmployees();

        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;
        double totalSSS = 0;
        double totalPhilhealth = 0;
        double totalPagibig = 0;
        double totalTax = 0;

        for (Employee e : employees) {
            double hours = attendanceService.getHoursForCutoff(
                    e.getEmployeeId(), cutoffPeriod);

            PayrollRecord record = processor.processPayroll(e, hours);

            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();
            totalSSS += record.getSss();
            totalPhilhealth += record.getPhilhealth();
            totalPagibig += record.getPagibig();
            totalTax += record.getTax();
        }

        return new PayrollSummary(
                totalGross,
                totalDeductions,
                totalNet,
                employees.size(),
                totalSSS,
                totalPhilhealth,
                totalPagibig,
                totalTax
        );
    }

    // ================= SUMMARY (legacy, fixed hours) =================

    public PayrollSummary generatePayrollSummary(double hoursWorked) {
        List<Employee> employees = employeeService.getAllEmployees();

        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;
        double totalSSS = 0;
        double totalPhilhealth = 0;
        double totalPagibig = 0;
        double totalTax = 0;

        for (Employee e : employees) {
            PayrollRecord record = processor.processPayroll(e, hoursWorked);

            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();
            totalSSS += record.getSss();
            totalPhilhealth += record.getPhilhealth();
            totalPagibig += record.getPagibig();
            totalTax += record.getTax();
        }

        return new PayrollSummary(
                totalGross,
                totalDeductions,
                totalNet,
                employees.size(),
                totalSSS,
                totalPhilhealth,
                totalPagibig,
                totalTax
        );
    }

    // ================= PROCESS & SAVE (attendance-based) =================

    public boolean processAndSavePayroll(String cutoffPeriod, boolean overwriteIfExists) {
        if (historyRepository.existsByCutoff(cutoffPeriod)) {
            if (!overwriteIfExists) {
                return false;
            }
            historyRepository.deleteByCutoff(cutoffPeriod);
        }

        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee e : employees) {
            double hours = attendanceService.getHoursForCutoff(
                    e.getEmployeeId(), cutoffPeriod);

            PayrollRecord record = processor.processPayroll(e, hours);

            PayrollHistoryRecord history = new PayrollHistoryRecord(
                    e.getEmployeeId(),
                    cutoffPeriod,
                    record.getBasicComponent(),
                    record.getAllowanceComponent(),
                    record.getGross(),
                    record.getSss(),
                    record.getPhilhealth(),
                    record.getPagibig(),
                    record.getTax(),
                    record.getTotalDeductions(),
                    record.getNet()
            );

            historyRepository.savePayrollRecord(history);
        }

        return true;
    }

    // ================= PROCESS & SAVE (legacy, fixed hours) =================

    public boolean processAndSavePayroll(double hoursWorked, String cutoffPeriod, boolean overwriteIfExists) {
        if (historyRepository.existsByCutoff(cutoffPeriod)) {
            if (!overwriteIfExists) {
                return false;
            }
            historyRepository.deleteByCutoff(cutoffPeriod);
        }

        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee e : employees) {
            PayrollRecord record = processor.processPayroll(e, hoursWorked);

            PayrollHistoryRecord history = new PayrollHistoryRecord(
                    e.getEmployeeId(),
                    cutoffPeriod,
                    record.getBasicComponent(),
                    record.getAllowanceComponent(),
                    record.getGross(),
                    record.getSss(),
                    record.getPhilhealth(),
                    record.getPagibig(),
                    record.getTax(),
                    record.getTotalDeductions(),
                    record.getNet()
            );

            historyRepository.savePayrollRecord(history);
        }

        return true;
    }

    // ================= ATTENDANCE INTEGRATION =================

    public List<String> getAvailableCutoffs() {
        return attendanceService.getAvailableCutoffs();
    }

    public double getHoursForCutoff(int employeeId, String cutoffPeriod) {
        return attendanceService.getHoursForCutoff(employeeId, cutoffPeriod);
    }

    // ================= SINGLE EMPLOYEE =================

    public PayrollRecord processPayrollForEmployee(Employee employee, double hoursWorked) {
        return processor.processPayroll(employee, hoursWorked);
    }

    // ================= HISTORY =================

    public List<PayrollHistoryRecord> getPayrollHistoryForEmployee(int employeeId) {
        return historyRepository.findByEmployeeId(employeeId);
    }

    public List<PayrollHistoryRecord> getPayrollHistoryByCutoff(String cutoffPeriod) {
        List<PayrollHistoryRecord> filtered = new ArrayList<>();

        for (PayrollHistoryRecord record : historyRepository.findAll()) {
            String recordCutoff = record.getCutoffPeriod() == null
                    ? ""
                    : record.getCutoffPeriod().trim();

            String selectedCutoff = cutoffPeriod == null
                    ? ""
                    : cutoffPeriod.trim();

            if (recordCutoff.equalsIgnoreCase(selectedCutoff)) {
                filtered.add(record);
            }
        }

        return filtered;
    }

    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    public List<PayrollHistoryRecord> getAllPayrollHistory() {
        return historyRepository.findAll();
    }

    public List<String> getProcessedCutoffs() {
        Set<String> cutoffs = new LinkedHashSet<>();

        for (PayrollHistoryRecord record : historyRepository.findAll()) {
            if (record.getCutoffPeriod() != null && !record.getCutoffPeriod().trim().isEmpty()) {
                cutoffs.add(record.getCutoffPeriod().trim());
            }
        }

        return new ArrayList<>(cutoffs);
    }

    public boolean exportPayrollHistoryByCutoff(String cutoffPeriod, String filePath) {
        List<PayrollHistoryRecord> records = getPayrollHistoryByCutoff(cutoffPeriod);

        if (records.isEmpty()) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("employeeId,cutoffPeriod,basicComponent,allowanceComponent,gross,sss,philhealth,pagibig,tax,totalDeductions,net");
            writer.newLine();

            for (PayrollHistoryRecord record : records) {
                writer.write(
                        record.getEmployeeId() + "," +
                        record.getCutoffPeriod() + "," +
                        record.getBasicComponent() + "," +
                        record.getAllowanceComponent() + "," +
                        record.getGross() + "," +
                        record.getSss() + "," +
                        record.getPhilhealth() + "," +
                        record.getPagibig() + "," +
                        record.getTax() + "," +
                        record.getTotalDeductions() + "," +
                        record.getNet()
                );
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteCutoff(String cutoffPeriod) {
        historyRepository.deleteByCutoff(cutoffPeriod);
    }
}