/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvEmployeeRepository implements EmployeeRepository {

    // Real file path for read/write
    private final String filePath = "src/main/resources/employees.csv";

    private static final String HEADER =
            "employeeId,firstName,lastName,birthday,address,phoneNumber,email,status,position,immediateSupervisor," +
            "basicSalary,riceSubsidy,phoneAllowance,clothingAllowance,grossSemiMonthlyRate,hourlyRate," +
            "sssNumber,philhealthNumber,tinNumber,pagibigNumber,username,password,role";

    // ================= ADD =================
    @Override
    public void addEmployee(Employee employee) {
        try {
            File file = new File(filePath);
            boolean needsNewLine = file.exists() && file.length() > 0;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                if (needsNewLine) {
                    bw.newLine();
                }
                bw.write(formatEmployee(employee));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    @Override
    public void updateEmployee(Employee updatedEmployee) {
        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(HEADER);
            bw.newLine();

            for (Employee emp : employees) {
                if (emp.getEmployeeId() == updatedEmployee.getEmployeeId()) {
                    emp = updatedEmployee;
                }
                bw.write(formatEmployee(emp));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @Override
    public void deleteEmployee(int employeeId) {
        List<Employee> employees = getAllEmployees();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(HEADER);
            bw.newLine();

            for (Employee emp : employees) {
                if (emp.getEmployeeId() != employeeId) {
                    bw.write(formatEmployee(emp));
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= FIND =================
    @Override
    public Employee findEmployee(int employeeId) {
        List<Employee> employees = getAllEmployees();

        for (Employee emp : employees) {
            if (emp.getEmployeeId() == employeeId) {
                return emp;
            }
        }

        return null;
    }

    // ================= READ ALL =================
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return employees;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = parseCsvLine(line);

                if (data.length < 23) {
                    System.out.println("Skipped invalid employee row: " + line);
                    continue;
                }

                try {
                    int employeeId = Integer.parseInt(data[0].trim());
                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String birthday = data[3].trim();
                    String address = data[4].trim();
                    String phoneNumber = data[5].trim();
                    String email = data[6].trim();
                    String employmentStatus = data[7].trim();
                    String position = data[8].trim();
                    String immediateSupervisor = data[9].trim();

                    double basicSalary = Double.parseDouble(data[10].trim());
                    double riceSubsidy = Double.parseDouble(data[11].trim());
                    double phoneAllowance = Double.parseDouble(data[12].trim());
                    double clothingAllowance = Double.parseDouble(data[13].trim());
                    double grossSemiMonthlyRate = Double.parseDouble(data[14].trim());
                    double hourlyRate = Double.parseDouble(data[15].trim());

                    String sssNumber = data[16].trim();
                    String philhealthNumber = data[17].trim();
                    String tinNumber = data[18].trim();
                    String pagibigNumber = data[19].trim();

                    String username = data[20].trim();
                    String password = data[21].trim();
                    String role = data[22].trim();

                    double totalAllowance = riceSubsidy + phoneAllowance + clothingAllowance;

                    Employee employee = new RegularEmployee(
                            employeeId,
                            firstName,
                            lastName,
                            position,
                            employmentStatus,
                            basicSalary,
                            totalAllowance,
                            hourlyRate,
                            username,
                            password,
                            role
                    );

                    // Extended fields
                    employee.setBirthday(birthday);
                    employee.setAddress(address);
                    employee.setPhoneNumber(phoneNumber);
                    employee.setEmail(email);
                    employee.setImmediateSupervisor(immediateSupervisor);

                    employee.setRiceSubsidy(riceSubsidy);
                    employee.setPhoneAllowance(phoneAllowance);
                    employee.setClothingAllowance(clothingAllowance);
                    employee.setGrossSemiMonthlyRate(grossSemiMonthlyRate);

                    employee.setSssNumber(sssNumber);
                    employee.setPhilhealthNumber(philhealthNumber);
                    employee.setTinNumber(tinNumber);
                    employee.setPagibigNumber(pagibigNumber);

                    employees.add(employee);

                } catch (NumberFormatException ex) {
                    System.out.println("Skipped malformed employee row: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }

    // ================= HELPER METHOD =================
    private String formatEmployee(Employee emp) {
        return emp.getEmployeeId() + "," +
                emp.getFirstName() + "," +
                emp.getLastName() + "," +
                safe(emp.getBirthday()) + "," +
                quote(safe(emp.getAddress())) + "," +
                safe(emp.getPhoneNumber()) + "," +
                safe(emp.getEmail()) + "," +
                emp.getEmploymentStatus() + "," +
                emp.getPosition() + "," +
                quote(safe(emp.getImmediateSupervisor())) + "," +
                emp.getBasicSalary() + "," +
                emp.getRiceSubsidy() + "," +
                emp.getPhoneAllowance() + "," +
                emp.getClothingAllowance() + "," +
                emp.getGrossSemiMonthlyRate() + "," +
                emp.getHourlyRate() + "," +
                safe(emp.getSssNumber()) + "," +
                safe(emp.getPhilhealthNumber()) + "," +
                safe(emp.getTinNumber()) + "," +
                safe(emp.getPagibigNumber()) + "," +
                emp.getUsername() + "," +
                emp.getPassword() + "," +
                emp.getRole();
    }

    // Handles commas inside quoted CSV fields
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values.toArray(new String[0]);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String quote(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}



// This version:
// This repository reads and writes employee data using a CSV file.
//
// Current CSV structure includes:
// - personal details
// - employment details
// - compensation breakdown
// - government IDs
// - login fields (username, password, role)
//
// The repository fully parses and preserves all 23 employee attributes.
// This keeps the expanded employee.csv structure aligned with the upgraded
// Employee model while preserving the current layered architecture.
//
// The repository supports:
// - addEmployee()
// - updateEmployee()
// - deleteEmployee()
// - findEmployee()
// - getAllEmployees()
//
// IMPORTANT:
// The column order in employees.csv must exactly match the HEADER constant.
// Any mismatch may cause incorrect parsing or skipped rows.
//
// parseCsvLine() is used instead of line.split(",") so quoted fields
// such as address and supervisor names are handled correctly.