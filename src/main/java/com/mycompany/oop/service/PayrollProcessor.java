/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;

public class PayrollProcessor {

    public PayrollRecord processPayroll(Employee emp, double hoursWorked) {

        PayrollRecord record = new PayrollRecord(emp, hoursWorked);

        // ================= GROSS COMPUTATION =================

        double basicHalf = emp.getBasicSalary() / 2;
        double allowanceHalf = emp.getAllowance() / 2;
        double gross = basicHalf + allowanceHalf;

        record.setBasicComponent(basicHalf);
        record.setAllowanceComponent(allowanceHalf);
        record.setGross(gross);

        // ================= MONTHLY CONVERSION =================

        double monthlyEquivalent = gross * 2;

        // ================= GOVERNMENT DEDUCTIONS (MONTHLY) =================

        double sss;
        if (monthlyEquivalent <= 3250) sss = 135;
        else if (monthlyEquivalent <= 3749.99) sss = 157.5;
        else if (monthlyEquivalent <= 4249.99) sss = 180;
        else if (monthlyEquivalent <= 4749.99) sss = 202.5;
        else if (monthlyEquivalent <= 5249.99) sss = 225;
        else sss = 247.5;

        double philhealth = Math.max(300,
                Math.min(monthlyEquivalent * 0.03, 1800));

        double pagibig =
                (monthlyEquivalent <= 1500)
                        ? monthlyEquivalent * 0.01
                        : monthlyEquivalent * 0.02;

        pagibig = Math.min(pagibig, 100);

        double tax;
        if (monthlyEquivalent <= 20832) tax = 0;
        else if (monthlyEquivalent < 33333)
            tax = (monthlyEquivalent - 20833) * 0.2;
        else if (monthlyEquivalent < 66667)
            tax = 2500 + (monthlyEquivalent - 33333) * 0.25;
        else if (monthlyEquivalent < 166667)
            tax = 10833 + (monthlyEquivalent - 66667) * 0.3;
        else
            tax = 40833.33 + (monthlyEquivalent - 166667) * 0.32;

        // ================= CONVERT TO SEMI-MONTHLY =================

        double cutoffSss = sss / 2;
        double cutoffPhilhealth = philhealth / 2;
        double cutoffPagibig = pagibig / 2;
        double cutoffTax = tax / 2;

        double totalDeductions =
                cutoffSss +
                cutoffPhilhealth +
                cutoffPagibig +
                cutoffTax;

        record.setSss(cutoffSss);
        record.setPhilhealth(cutoffPhilhealth);
        record.setPagibig(cutoffPagibig);
        record.setTax(cutoffTax);
        record.setTotalDeductions(totalDeductions);

        record.setNet(gross - totalDeductions);

        return record;
    }
    
    public PayrollRecord processPayroll(Employee emp) {
        double defaultHours = 80;
        return processPayroll(emp, defaultHours);
    }

}

/*
PAYROLL PROCESSOR – COMPUTATION ENGINE SUMMARY

Purpose:
Handles payroll computation for an employee per payroll cutoff period.

Core Responsibility:
• Compute semi-monthly gross pay
• Convert cutoff gross to monthly equivalent for deduction bracket evaluation
• Compute government deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax)
• Convert deductions back to cutoff values
• Return a PayrollRecord object containing the full payroll breakdown

Gross Salary Logic:
• Gross Pay is computed using the standard MotorPH rule:
      Gross (cutoff) = (Basic Salary / 2) + (Allowance / 2)

• Basic Salary and Allowance are treated as monthly values and divided
  equally per payroll cutoff.

Deduction Logic:
Government deductions are evaluated using the **monthly equivalent salary**
to determine the correct contribution bracket.

Steps:
1. Compute cutoff gross salary
2. Convert cutoff gross to monthly equivalent (gross × 2)
3. Determine monthly statutory deductions:
      - SSS
      - PhilHealth
      - Pag-IBIG
      - Withholding Tax
4. Convert deductions back to cutoff values (divide by 2)
5. Compute final net salary:

      Net Salary = Gross (cutoff) − Total Deductions (cutoff)

Architectural Design:
• Payroll computation is handled entirely in the Service layer
• UI components do not perform payroll calculations
• PayrollRecord encapsulates the payroll breakdown cleanly
• Employee model only provides base salary data and remains payroll-agnostic

Design Principle:
Stateless computation engine.

The processor does not persist data internally.
It accepts input (Employee + hoursWorked) and produces a deterministic
PayrollRecord output.

Benefits:
• Ensures consistent payroll calculations across the system
• Prevents mismatch between displayed payslip breakdown and totals
• Keeps business logic centralized and maintainable
• Supports future payroll policy changes without UI modification

Possible Future Enhancements:
• Replace simplified SSS logic with full official contribution table
• Externalize tax brackets to configuration
• Dynamic PhilHealth rate updates
• Attendance-based payroll computation
• Versioned payroll policy support
*/