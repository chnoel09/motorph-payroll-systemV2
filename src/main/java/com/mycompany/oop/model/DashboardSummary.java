/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public class DashboardSummary {

    private int totalEmployees;
    private int activeEmployees;
    private int pendingLeaves;

    private int adminCount;
    private int hrCount;
    private int financeCount;
    private int employeeCount;
    private int itCount;

    private double totalGross;
    private double totalNet;
    private double totalDeductions;
    private double totalAllowance;

    public DashboardSummary(
            int totalEmployees,
            int activeEmployees,
            int pendingLeaves,
            int adminCount,
            int hrCount,
            int financeCount,
            int employeeCount,
            int itCount,
            double totalGross,
            double totalNet,
            double totalDeductions,
            double totalAllowance
    ) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.pendingLeaves = pendingLeaves;
        this.adminCount = adminCount;
        this.hrCount = hrCount;
        this.financeCount = financeCount;
        this.employeeCount = employeeCount;
        this.itCount = itCount;
        this.totalGross = totalGross;
        this.totalNet = totalNet;
        this.totalDeductions = totalDeductions;
        this.totalAllowance = totalAllowance;
    }

    public int getTotalEmployees() { return totalEmployees; }
    public int getActiveEmployees() { return activeEmployees; }
    public int getPendingLeaves() { return pendingLeaves; }

    public int getAdminCount() { return adminCount; }
    public int getHrCount() { return hrCount; }
    public int getFinanceCount() { return financeCount; }
    public int getEmployeeCount() { return employeeCount; }
    public int getItCount() { return itCount; }

    public double getTotalGross() { return totalGross; }
    public double getTotalNet() { return totalNet; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getTotalAllowance() { return totalAllowance; }
}

/*
===============================================================================
MAIN APPLICATION FRAME – SYSTEM OVERVIEW
-------------------------------------------------------------------------------

Purpose:
MainAppFrame serves as the central container of the MotorPH Payroll System.

It is responsible for:
- Rendering the main window after login
- Building role-based navigation
- Managing panel switching using CardLayout
- Coordinating all functional modules (HR, Payroll, Payslip, Leave, etc.)

-------------------------------------------------------------------------------
ARCHITECTURE ROLE
-------------------------------------------------------------------------------

MainAppFrame acts as the UI Shell of the system.

It does NOT:
- Compute payroll
- Access CSV files
- Perform business logic

Instead, it:
- Delegates logic to Service classes
- Delegates computation to Processor classes
- Displays View panels
- Switches panels dynamically

This follows Separation of Concerns principle.

-------------------------------------------------------------------------------
ROLE-BASED NAVIGATION
-------------------------------------------------------------------------------

After login, the logged-in Employee object is passed into MainAppFrame.

Navigation buttons are generated based on role:

Admin:
    - Employees
    - Payroll
    - Leave Review

HR:
    - Employees
    - Leave Review

Finance:
    - Payroll

Employee:
    - My Profile
    - File Leave
    - Payslip

IT:
    - User Management

This ensures:
- Access control by role
- Clean UI per user type
- No unnecessary module exposure

-------------------------------------------------------------------------------
CARD LAYOUT SYSTEM
-------------------------------------------------------------------------------

MainAppFrame uses CardLayout to switch between panels.

Each panel is registered with a unique key:
    "DASH"
    "EMP"
    "PAYROLL"
    "PAYSLIP"
    "FILE"
    "LEAVE"
    "PROFILE"
    "IT"

When a sidebar button is clicked:
    cardLayout.show(contentPanel, key);

This allows:
- Fast UI switching
- No need to recreate panels
- Clean modular structure

-------------------------------------------------------------------------------
MODULE INTEGRATION
-------------------------------------------------------------------------------

MainAppFrame integrates:

DashboardPanel
HRPanel
PayrollPanel
PayslipPanel
LeavePanel
LeaveReviewPanel
EmployeePanel
ITPanel

Each panel handles its own business interactions
through its respective Service layer.

-------------------------------------------------------------------------------
LOGOUT FLOW
-------------------------------------------------------------------------------

Logout button:
- Disposes current MainAppFrame
- Reopens LoginFrame
- Resets application state

-------------------------------------------------------------------------------
DESIGN PATTERNS USED
-------------------------------------------------------------------------------

1. MVC-inspired structure
   - View: Panels
   - Model: Employee, PayrollRecord, etc.
   - Service: PayrollService, EmployeeService

2. Role-Based Access Control

3. CardLayout for View Switching

4. Separation of Concerns

-------------------------------------------------------------------------------
SCALABILITY
-------------------------------------------------------------------------------

MainAppFrame can easily support:
- Additional roles
- Additional modules
- More complex permission logic
- Multi-window extensions

-------------------------------------------------------------------------------
SYSTEM FLOW SUMMARY
-------------------------------------------------------------------------------

LoginFrame → validates user → passes Employee object →
MainAppFrame → builds role-based navigation →
User selects module → CardLayout switches panel →
Panel calls Service → Service calls Repository →
Repository reads/writes CSV →
UI updates with results

-------------------------------------------------------------------------------
This class serves as the structural backbone of the entire application.
===============================================================================
*/