/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public class RegularEmployee extends Employee {
    //RegularEmployee = a real employee type that can be paid    

    
    
    public RegularEmployee(
            int employeeId, 
            String firstName, 
            String lastName, 
            String position, 
            String employmentStatus, 
            double basicSalary, 
            double allowance, 
            double hourlyRate,
            String username,
            String password,
            String role

    ) {
        
        super(
                employeeId, 
                firstName, 
                lastName, 
                position, 
                employmentStatus, 
                basicSalary, 
                allowance, 
                hourlyRate,
                username,
                password,
                role
        
        );

                

    }    
   
   //ABSTRACT METHOD
    
    @Override
    public double computeGrossSalary() {
        return getBasicSalary() + getAllowance();
    } 
    
    @Override
    public double computeDeductions() {
        return 0; // handled by PayrollProcessor
    }
}










/*
REGULAR EMPLOYEE – CONCRETE EMPLOYEE TYPE

Purpose:
Represents a standard full-time employee within the MotorPH Payroll System.

Role in the System:
This class is a concrete implementation of the abstract Employee class.
It defines how salary-related template methods behave for regular employees.

Key Responsibilities:
• Provide implementation for payroll-related methods required by Employee
• Supply the base gross salary computation logic
• Serve as the default employee type loaded from the CSV data source

Payroll Behavior:
• Gross Salary = Basic Salary + Allowance
• Government deductions are NOT computed here

Important Design Decision:
All statutory deduction calculations (SSS, PhilHealth, Pag-IBIG, Withholding Tax)
are handled in the PayrollProcessor service class.

This keeps the Employee model focused only on employee data while allowing
the service layer to manage payroll policies and computation logic.

OOP Principles Demonstrated:
• Inheritance – extends the abstract Employee class
• Polymorphism – implements abstract payroll methods
• Encapsulation – employee data is accessed via getters/setters

Design Philosophy:
The Employee model remains payroll-agnostic while providing the base
structure needed for payroll processing.
*/