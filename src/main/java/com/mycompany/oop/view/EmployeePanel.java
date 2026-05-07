/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import com.mycompany.oop.model.Employee;

public class EmployeePanel extends JPanel {

    private Employee employee;

    public EmployeePanel(Employee employee) {
        this.employee = employee;

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("My Profile"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UITheme.BG);

        NumberFormat peso = NumberFormat.getCurrencyInstance(
                new Locale("en", "PH")
        );

        mainPanel.add(createSection("Personal Information", new String[][]{
                {"Employee ID", String.valueOf(employee.getEmployeeId())},
                {"Name", employee.getFirstName() + " " + employee.getLastName()},
                {"Birthday", safe(employee.getBirthday())},
                {"Address", safe(employee.getAddress())},
                {"Phone Number", safe(employee.getPhoneNumber())},
                {"Email", safe(employee.getEmail())}
        }));

        mainPanel.add(Box.createVerticalStrut(16));

        mainPanel.add(createSection("Employment Details", new String[][]{
                {"Position", safe(employee.getPosition())},
                {"Status", safe(employee.getEmploymentStatus())},
                {"Role", safe(employee.getRole())},
                {"Username", safe(employee.getUsername())},
                {"Immediate Supervisor", safe(employee.getImmediateSupervisor())}
        }));

        mainPanel.add(Box.createVerticalStrut(16));

        mainPanel.add(createSection("Government Information", new String[][]{
                {"SSS Number", safe(employee.getSssNumber())},
                {"PhilHealth Number", safe(employee.getPhilhealthNumber())},
                {"TIN Number", safe(employee.getTinNumber())},
                {"Pag-IBIG Number", safe(employee.getPagibigNumber())}
        }));

        mainPanel.add(Box.createVerticalStrut(16));

        mainPanel.add(createSection("Salary Details", new String[][]{
                {"Basic Salary", peso.format(employee.getBasicSalary())},
                {"Rice Subsidy", peso.format(employee.getRiceSubsidy())},
                {"Phone Allowance", peso.format(employee.getPhoneAllowance())},
                {"Clothing Allowance", peso.format(employee.getClothingAllowance())},
                {"Total Allowance", peso.format(employee.getAllowance())},
                {"Gross Semi-Monthly Rate", peso.format(employee.getGrossSemiMonthlyRate())},
                {"Hourly Rate", peso.format(employee.getHourlyRate())},
                {"Gross Salary", peso.format(employee.computeGrossSalary())},
                {"Net Salary", peso.format(employee.computeNetSalary())}
        }));

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(UITheme.BG);

        content.add(scroll, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createSection(String title, String[][] rows) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(18, 22, 18, 22)
        ));
        section.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                70 + rows.length * 30
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.FONT_SECTION);
        lblTitle.setForeground(UITheme.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel body = new JPanel(new GridLayout(0, 1, 0, 8));
        body.setBackground(Color.WHITE);

        for (String[] row : rows) {
            body.add(createRow(row[0], row[1]));
        }

        section.add(lblTitle, BorderLayout.NORTH);
        section.add(body, BorderLayout.CENTER);

        return section;
    }

    private JPanel createRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_SECONDARY);

        JLabel val = new JLabel(value != null && !value.isBlank() ? value : "N/A");
        val.setFont(UITheme.FONT_BODY_BOLD);
        val.setForeground(UITheme.TEXT_PRIMARY);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);

        return row;
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}