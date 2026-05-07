/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.model;

public class PayrollHistoryRecord {

    private int employeeId;
    private String cutoffPeriod;

    private double gross;
    private double sss;
    private double philhealth;
    private double pagibig;
    private double tax;
    private double totalDeductions;
    private double net;   
    private double basicComponent;
    private double allowanceComponent;

    public PayrollHistoryRecord(
            int employeeId,
            String cutoffPeriod,
            double basicComponent,
            double allowanceComponent,
            double gross,
            double sss,
            double philhealth,
            double pagibig,
            double tax,
            double totalDeductions,
            double net
    ) {
        this.employeeId = employeeId;
        this.cutoffPeriod = cutoffPeriod;
        this.basicComponent = basicComponent;
        this.allowanceComponent = allowanceComponent;
        this.gross = gross;
        this.sss = sss;
        this.philhealth = philhealth;
        this.pagibig = pagibig;
        this.tax = tax;
        this.totalDeductions = totalDeductions;
        this.net = net;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getCutoffPeriod() {
        return cutoffPeriod;
    }

    public double getGross() {
        return gross;
    }

    public double getSss() {
        return sss;
    }

    public double getPhilhealth() {
        return philhealth;
    }

    public double getPagibig() {
        return pagibig;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public double getNet() {
        return net;
    }
    
    public double getBasicComponent() { 
        return basicComponent; 
    
    }
    public double getAllowanceComponent() { 
        return allowanceComponent; 
    }
    
}
