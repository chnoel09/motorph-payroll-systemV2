/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.Employee;
// We import Employee because this interface will work with Employee objects

import java.util.List;
// We import List because we will return multiple employees


 // This is a CONTRACT.
 // It defines WHAT operations are allowed for employee data.
 // It does NOT define HOW they work.
 // Any class that implements this interface MUST provide
 // the actual logic for these methods.

public interface EmployeeRepository {
// Adds a new employee to the data source.
// Parameter:
    // - employee > the Emplotee object we want to store

// Return type:
    // - void > means this method does not return anything
    
    void addEmployee(Employee employee);                              
    void updateEmployee(Employee employee);                          
    void deleteEmployee(int employeeId);                              
    Employee findEmployee(int employeeId);
    List<Employee> getAllEmployees();
    
}
