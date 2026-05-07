/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.Leave;
import com.mycompany.oop.service.LeaveService;
import com.toedter.calendar.JDateChooser;

public class LeavePanel extends JPanel {

    private LeaveService service;
    private JTable table;
    private Employee employee;

    public LeavePanel(Employee employee) {

        this.employee = employee;
        this.service = new LeaveService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("File Leave Request"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createFormPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel formTitle = new JLabel("New Leave Request");
        formTitle.setFont(UITheme.FONT_SECTION);
        formTitle.setForeground(UITheme.TEXT_PRIMARY);
        formTitle.setBorder(new EmptyBorder(0, 0, 14, 0));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 12, 10));
        formPanel.setBackground(Color.WHITE);

        JComboBox<String> typeBox = new JComboBox<>(
                new String[]{"Vacation", "Sick", "Emergency"});
        styleField(typeBox);

        JDateChooser startChooser = new JDateChooser();
        startChooser.setDateFormatString("yyyy-MM-dd");
        startChooser.setDate(new Date());
        styleDateChooser(startChooser);

        JDateChooser endChooser = new JDateChooser();
        endChooser.setDateFormatString("yyyy-MM-dd");
        endChooser.setDate(new Date());
        styleDateChooser(endChooser);

        JTextField reasonField = new JTextField();
        styleTextField(reasonField);

        formPanel.add(createLabel("Leave Type:"));
        formPanel.add(typeBox);

        formPanel.add(createLabel("Start Date:"));
        formPanel.add(startChooser);

        formPanel.add(createLabel("End Date:"));
        formPanel.add(endChooser);

        formPanel.add(createLabel("Reason:"));
        formPanel.add(reasonField);

        JButton submitBtn = UITheme.createAccentButton("Submit Leave");
        submitBtn.setFocusable(false);
        submitBtn.setPreferredSize(new Dimension(140, 36));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        buttonRow.setBackground(Color.WHITE);
        buttonRow.add(submitBtn);

        formWrapper.add(formTitle, BorderLayout.NORTH);
        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(buttonRow, BorderLayout.SOUTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        submitBtn.addActionListener(e -> {
            try {
                if (startChooser.getDate() == null || endChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please select both start and end dates.",
                            "Missing Date",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String startDate = sdf.format(startChooser.getDate());
                String endDate = sdf.format(endChooser.getDate());

                Leave leave = service.fileLeave(
                        employee.getEmployeeId(),
                        typeBox.getSelectedItem().toString(),
                        startDate,
                        endDate,
                        reasonField.getText().trim()
                );

                JOptionPane.showMessageDialog(this,
                        "Leave filed successfully.\nStatus: " + leave.getStatus());

                refreshTable();

                startChooser.setDate(new Date());
                endChooser.setDate(new Date());
                reasonField.setText("");
                typeBox.setSelectedIndex(0);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(UITheme.BG);
        outerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        outerWrapper.add(formWrapper, BorderLayout.CENTER);

        return outerWrapper;
    }

    private JScrollPane createTablePanel() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        List<Leave> leaves =
                service.getLeavesByEmployee(employee.getEmployeeId());

        String[] columns = {
                "Leave ID", "Type", "Start", "End", "Reason", "Status"
        };

        Object[][] data = new Object[leaves.size()][6];

        for (int i = 0; i < leaves.size(); i++) {
            Leave l = leaves.get(i);
            data[i][0] = l.getLeaveId();
            data[i][1] = l.getLeaveType();
            data[i][2] = l.getStartDate();
            data[i][3] = l.getEndDate();
            data[i][4] = l.getReason();
            data[i][5] = l.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }

    private void styleField(JComboBox<String> field) {
        field.setFont(UITheme.FONT_BODY);
        field.setBackground(Color.WHITE);
    }

    private void styleTextField(JTextField field) {
        field.setFont(UITheme.FONT_BODY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void styleDateChooser(JDateChooser chooser) {
        chooser.setFont(UITheme.FONT_BODY);
        chooser.setBackground(Color.WHITE);

        JTextField editor = ((JTextField) chooser.getDateEditor().getUiComponent());
        editor.setFont(UITheme.FONT_BODY);
        editor.setBackground(Color.WHITE);
        editor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
    }
}