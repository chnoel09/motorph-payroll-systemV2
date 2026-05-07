/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oop.model;

public interface Payables {
    //Payables = what a payable object CAN DO

    
    //refer to MS1 Worksheet method dictionary for the list.    
    // Computes total gross salary (basic + overtime + allowances)
    double computeGrossSalary();
    double computeDeductions();
    double computeNetSalary();

     
    // Generates and returns a payroll record object
    // (create PayrollRecord later)
    // PayrollRecord generatePayroll();

    // Explanation:
    // The Payables interface abstracts payroll behaviour.
    // It defines WHAT payroll computations must exist, but NOT HOW they are computed.
    // The actual logic will be implemented by service classes.

    // Next step is to create a class that implements it.
    // public class PayrollService implements Payables
    // This is where the real formulas go, employee data is used, polymorphism starts to appear.
}




//STEP 0 — Lock the mindset first (VERY IMPORTANT)
//Reflection:
//Encapsulation = protect data
//Abstraction = define behavior

//Encapsulation answered:
//“How do I protect employee data?”

//Encapsulation answered:
//“How do I protect employee data?”

//STEP 1 — Identify WHAT behavior we want (not HOW)
//“What should the system be able to do with an employee?”
//Compute pay
//Generate salary-related results
//“Anything that gets paid should be payable.” <---this is ABSTRACTION

//STEP 2 — Introduce a NEW concept: Interface
//An interface is a list of method promises.
//what methods MUST exist
//but NOT how they are implemented
//a checklist that a class must follow

//STEP 3 — Decide the interface name (naming matters)
//We choose a name that describes behavior, not data.
//Here we use "Payable".

//STEP 4 — Create the interface (NEW FILE)
//Placed under SERVICE PACKAGE as per demo