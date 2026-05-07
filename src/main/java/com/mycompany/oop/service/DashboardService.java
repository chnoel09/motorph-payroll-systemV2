/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.Leave;
import com.mycompany.oop.model.DashboardSummary;

import java.util.List;

public class DashboardService {

    private EmployeeService employeeService;
    private LeaveService leaveService;
    private PayrollProcessor processor;

    public DashboardService() {
        this.employeeService = new EmployeeService();
        this.leaveService = new LeaveService();
        this.processor = new PayrollProcessor();
    }

    public DashboardSummary generateSummary() {

        List<Employee> employees = employeeService.getAllEmployees();
        List<Leave> leaves = leaveService.getAllLeaves();

        int totalEmployees = employees.size();

        int activeEmployees = (int) employees.stream()
                .filter(e -> !e.getEmploymentStatus()
                        .equalsIgnoreCase("Inactive"))
                .count();

        int pendingLeaves = (int) leaves.stream()
                .filter(l -> l.getStatus().equalsIgnoreCase("PENDING"))
                .count();

        int adminCount = 0;
        int hrCount = 0;
        int financeCount = 0;
        int employeeCount = 0;
        int itCount = 0;

        double totalGross = 0;
        double totalNet = 0;
        double totalDeductions = 0;
        double totalAllowance = 0;

        for (Employee e : employees) {

            switch (e.getRole()) {
                case "Admin": adminCount++; break;
                case "HR": hrCount++; break;
                case "Finance": financeCount++; break;
                case "Employee": employeeCount++; break;
                case "IT": itCount++; break;
            }

            totalGross += e.computeGrossSalary();
            totalNet += e.computeNetSalary();
            totalDeductions += e.computeDeductions();
            totalAllowance += e.getAllowance();
        }

        return new DashboardSummary(
                totalEmployees,
                activeEmployees,
                pendingLeaves,
                adminCount,
                hrCount,
                financeCount,
                employeeCount,
                itCount,
                totalGross,
                totalNet,
                totalDeductions,
                totalAllowance
        );
    }
}

