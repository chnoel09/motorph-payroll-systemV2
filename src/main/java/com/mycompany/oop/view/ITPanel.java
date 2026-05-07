/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.EmployeeService;

public class ITPanel extends JPanel {

    private EmployeeService service;
    private JTable table;

    private JPanel cardsPanel;
    private JPanel content;

    private static final Color[] METRIC_ACCENTS = {
            new Color(210, 43, 43),
            new Color(37, 99, 195),
            new Color(59, 130, 246),
            new Color(34, 160, 70),
            new Color(185, 30, 30),
            new Color(130, 80, 210)
    };

    public ITPanel() {

        service = new EmployeeService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("System Administration"), BorderLayout.NORTH);

        content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        cardsPanel = createDashboardCards();

        content.add(cardsPanel, BorderLayout.NORTH);
        content.add(createUserTable(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createDashboardCards() {

        JPanel panel = new JPanel(new GridLayout(2, 3, 14, 14));
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        Map<String, Integer> counts = service.getUserRoleCounts();

        int total = service.getTotalUsers();
        int admin = counts.getOrDefault("Admin", 0);
        int hr = counts.getOrDefault("HR", 0);
        int finance = counts.getOrDefault("Finance", 0);
        int employee = counts.getOrDefault("Employee", 0);
        int it = counts.getOrDefault("IT", 0);

        panel.add(createCard("Total Users", total, 0));
        panel.add(createCard("Admins", admin, 1));
        panel.add(createCard("HR Users", hr, 2));
        panel.add(createCard("Finance Users", finance, 3));
        panel.add(createCard("Employees", employee, 4));
        panel.add(createCard("IT Users", it, 5));

        return panel;
    }

    private JPanel createCard(String title, int value, int accentIndex) {

        Color accent = METRIC_ACCENTS[accentIndex % METRIC_ACCENTS.length];

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(accent);
                g2.fillRect(0, 0, getWidth(), 3);
                g2.dispose();
            }
        };

        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(16, 18, 14, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_CARD_LABEL);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(6, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JScrollPane createUserTable() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        List<Employee> list = service.getAllEmployees();

        String[] columns = {"Employee ID", "Username", "Role", "Status"};
        Object[][] data = new Object[list.size()][4];

        for (int i = 0; i < list.size(); i++) {
            Employee e = list.get(i);
            data[i][0] = e.getEmployeeId();
            data[i][1] = e.getUsername();
            data[i][2] = e.getRole();
            data[i][3] = e.getEmploymentStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private void refreshPanel() {
        refreshTable();

        content.remove(cardsPanel);
        cardsPanel = createDashboardCards();
        content.add(cardsPanel, BorderLayout.NORTH);

        content.revalidate();
        content.repaint();
    }

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        panel.setBackground(UITheme.BG);

        JButton resetBtn = UITheme.createButton("Reset Password");
        JButton roleBtn = UITheme.createAccentButton("Change Role");

        resetBtn.setPreferredSize(new Dimension(160, 36));
        roleBtn.setPreferredSize(new Dimension(160, 36));

        resetBtn.addActionListener(e -> resetPassword());
        roleBtn.addActionListener(e -> changeRole());

        panel.add(resetBtn);
        panel.add(roleBtn);

        return panel;
    }

    private void resetPassword() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());

        String newPass = JOptionPane.showInputDialog(
                this,
                "Enter new password:"
        );

        if (newPass != null && !newPass.isEmpty()) {
            service.resetPassword(id, newPass);
            refreshPanel();
        }
    }

    private void changeRole() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());

        String newRole = (String) JOptionPane.showInputDialog(
                this,
                "Select new role:",
                "Change Role",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Admin", "HR", "Finance", "Employee", "IT"},
                "Employee"
        );

        if (newRole != null) {
            service.changeRole(id, newRole);
            refreshPanel();
        }
    }
}