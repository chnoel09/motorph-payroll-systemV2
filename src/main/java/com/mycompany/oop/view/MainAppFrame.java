/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.oop.model.Employee;

public class MainAppFrame extends JFrame {

    private Employee employee;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private List<JButton> navButtons = new ArrayList<>();
    private JButton activeButton;

    private HRPanel hrPanel;
    private ITPanel itPanel;
    private PayrollPanel payrollPanel;
    private PayslipPanel payslipPanel;
    private LeavePanel leavePanel;
    private LeaveReviewPanel leaveReviewPanel;
    private EmployeePanel employeePanel;
    private AttendancePanel attendancePanel;
    private HRPayrollHistoryPanel payrollHistoryPanel;
    private DashboardPanel dashboardPanel;

    public MainAppFrame(Employee employee) {

        this.employee = employee;

        setTitle("MotorPH Payroll System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG);
        getRootPane().setDefaultButton(null);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setLayout(new BorderLayout());

        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(UITheme.SIDEBAR_BG);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(28, 22, 16, 22));

        JLabel logo = new JLabel("MotorPH");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(UITheme.ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel(
                employee.getFirstName() + " • " + employee.getRole()
        );
        userLabel.setFont(UITheme.FONT_SMALL);
        userLabel.setForeground(UITheme.TEXT_SIDEBAR);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandPanel.add(logo);
        brandPanel.add(Box.createVerticalStrut(6));
        brandPanel.add(userLabel);

        sidebar.add(brandPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UITheme.SIDEBAR_BG);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new EmptyBorder(8, 8, 12, 8));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(35, 55, 95));
        sep.setBackground(UITheme.SIDEBAR_BG);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        buttonPanel.add(sep);
        buttonPanel.add(Box.createVerticalStrut(12));

        sidebar.add(buttonPanel, BorderLayout.CENTER);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BG);

        dashboardPanel = new DashboardPanel(employee);
        hrPanel = new HRPanel(employee);
        payrollPanel = new PayrollPanel();
        payslipPanel = new PayslipPanel(employee);
        leavePanel = new LeavePanel(employee);
        leaveReviewPanel = new LeaveReviewPanel();
        employeePanel = new EmployeePanel(employee);
        attendancePanel = new AttendancePanel(employee);
        itPanel = new ITPanel();
        payrollHistoryPanel = new HRPayrollHistoryPanel();

        contentPanel.add(dashboardPanel, "DASH");
        contentPanel.add(hrPanel, "EMP");
        contentPanel.add(payrollPanel, "PAYROLL");
        contentPanel.add(payslipPanel, "PAYSLIP");
        contentPanel.add(leavePanel, "FILE");
        contentPanel.add(leaveReviewPanel, "LEAVE");
        contentPanel.add(employeePanel, "PROFILE");
        contentPanel.add(attendancePanel, "ATTENDANCE");
        contentPanel.add(itPanel, "IT");
        contentPanel.add(payrollHistoryPanel, "PAYROLL_HISTORY");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        String role = employee.getRole() == null ? "" : employee.getRole().trim().toLowerCase();

        addNavButton(buttonPanel, "Dashboard", "DASH");

        if ("admin".equals(role)) {
            addNavButton(buttonPanel, "Employees", "EMP");
            addNavButton(buttonPanel, "Payroll", "PAYROLL");
            addNavButton(buttonPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(buttonPanel, "Leave Review", "LEAVE");
            addNavButton(buttonPanel, "User Management", "IT");

        } else if ("hr".equals(role)) {
            addNavButton(buttonPanel, "Employees", "EMP");
            addNavButton(buttonPanel, "Payroll History", "PAYROLL_HISTORY");
            addNavButton(buttonPanel, "Leave Review", "LEAVE");

        } else if ("finance".equals(role)) {
            addNavButton(buttonPanel, "Payroll", "PAYROLL");
            addNavButton(buttonPanel, "Payroll History", "PAYROLL_HISTORY");

        } else if ("employee".equals(role)) {
            addNavButton(buttonPanel, "My Profile", "PROFILE");
            addNavButton(buttonPanel, "Attendance", "ATTENDANCE");
            addNavButton(buttonPanel, "Payslip", "PAYSLIP");
            addNavButton(buttonPanel, "File Leave", "FILE");

        } else if ("it".equals(role)) {
            addNavButton(buttonPanel, "User Management", "IT");
        }

        if (!navButtons.isEmpty()) {
            setActiveButton(navButtons.get(0));
        }

        buttonPanel.add(Box.createVerticalGlue());

        JButton logoutBtn = UITheme.createSidebarDangerButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createVerticalStrut(12));

        cardLayout.show(contentPanel, "DASH");
    }

    private void addNavButton(JPanel panel, String text, String card) {
        JButton btn = UITheme.createSidebarButton(text);
        navButtons.add(btn);

        btn.addActionListener(e -> {
            refreshPanel(card);
            contentPanel.revalidate();
            contentPanel.repaint();
            cardLayout.show(contentPanel, card);
            setActiveButton(btn);
        });

        panel.add(btn);
        panel.add(Box.createVerticalStrut(4));
    }

    private void refreshPanel(String card) {

        if ("EMP".equals(card)) {
            contentPanel.remove(hrPanel);
            hrPanel = new HRPanel(employee);
            contentPanel.add(hrPanel, "EMP");
        }

        if ("IT".equals(card)) {
            contentPanel.remove(itPanel);
            itPanel = new ITPanel();
            contentPanel.add(itPanel, "IT");
        }

        if ("PAYROLL".equals(card)) {
            contentPanel.remove(payrollPanel);
            payrollPanel = new PayrollPanel();
            contentPanel.add(payrollPanel, "PAYROLL");
        }

        if ("PAYROLL_HISTORY".equals(card)) {
            contentPanel.remove(payrollHistoryPanel);
            payrollHistoryPanel = new HRPayrollHistoryPanel();
            contentPanel.add(payrollHistoryPanel, "PAYROLL_HISTORY");
        }

        if ("PROFILE".equals(card)) {
            contentPanel.remove(employeePanel);
            employeePanel = new EmployeePanel(employee);
            contentPanel.add(employeePanel, "PROFILE");
        }

        if ("ATTENDANCE".equals(card)) {
            contentPanel.remove(attendancePanel);
            attendancePanel = new AttendancePanel(employee);
            contentPanel.add(attendancePanel, "ATTENDANCE");
        }

        if ("PAYSLIP".equals(card)) {
            contentPanel.remove(payslipPanel);
            payslipPanel = new PayslipPanel(employee);
            contentPanel.add(payslipPanel, "PAYSLIP");
        }

        if ("FILE".equals(card)) {
            contentPanel.remove(leavePanel);
            leavePanel = new LeavePanel(employee);
            contentPanel.add(leavePanel, "FILE");
        }

        if ("LEAVE".equals(card)) {
            contentPanel.remove(leaveReviewPanel);
            leaveReviewPanel = new LeaveReviewPanel();
            contentPanel.add(leaveReviewPanel, "LEAVE");
        }

        if ("DASH".equals(card)) {
            contentPanel.remove(dashboardPanel);
            dashboardPanel = new DashboardPanel(employee);
            contentPanel.add(dashboardPanel, "DASH");
        }
    }

    private void setActiveButton(JButton selected) {
        for (JButton btn : navButtons) {
            btn.putClientProperty("sidebar.active", false);
            btn.setBackground(UITheme.SIDEBAR_BG);
            btn.setForeground(UITheme.TEXT_SIDEBAR);
        }

        activeButton = selected;
        activeButton.putClientProperty("sidebar.active", true);
        activeButton.setBackground(UITheme.SIDEBAR_HOVER);
        activeButton.setForeground(Color.WHITE);
    }
}

/*
MAIN APP FRAME – RBAC AND PANEL REFRESH UPDATE

Enhancement Summary:
This frame was refined to strengthen role-based navigation and ensure
updated panels are reloaded when selected.

Key Improvements:
• Role checks are normalized to avoid case or spacing issues.
• The HR panel now receives the logged-in employee context, allowing
  Admin and HR behavior to be handled correctly.
• Panels are refreshed on navigation to show the latest CSV-based data.

Impact:
• More reliable RBAC enforcement
• Better consistency between login role and visible modules
• Improved UI refresh behavior after updates
*/