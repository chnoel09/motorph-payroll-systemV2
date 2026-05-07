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
import com.mycompany.oop.service.LeaveService;
import com.mycompany.oop.model.Leave;

public class LeaveReviewPanel extends JPanel {

    private LeaveService service;
    private JTable table;

    public LeaveReviewPanel() {

        service = new LeaveService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Leave Approval Center"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    private JScrollPane createTablePanel() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        List<Leave> leaves = service.getAllLeaves();

        String[] cols = {
                "Leave ID",
                "Employee ID",
                "Type",
                "Start Date",
                "End Date",
                "Reason",
                "Status"
        };

        Object[][] data = new Object[leaves.size()][7];

        for (int i = 0; i < leaves.size(); i++) {
            Leave l = leaves.get(i);
            data[i][0] = l.getLeaveId();
            data[i][1] = l.getEmployeeId();
            data[i][2] = l.getLeaveType();
            data[i][3] = l.getStartDate();
            data[i][4] = l.getEndDate();
            data[i][5] = l.getReason();
            data[i][6] = l.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        JButton approveBtn = UITheme.createAccentButton("Approve");
        JButton rejectBtn = UITheme.createCrudDangerButton("Reject");

        approveBtn.setPreferredSize(new Dimension(110, 34));
        rejectBtn.setPreferredSize(new Dimension(110, 34));

        approveBtn.addActionListener(e -> updateStatus("APPROVED"));
        rejectBtn.addActionListener(e -> updateStatus("REJECTED"));

        panel.add(approveBtn);
        panel.add(rejectBtn);

        return panel;
    }

    private void updateStatus(String status) {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a leave request first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int leaveId = Integer.parseInt(table.getValueAt(row, 0).toString());
        String currentStatus = table.getValueAt(row, 6).toString();

        if (!"PENDING".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "This leave has already been " + currentStatus.toLowerCase() + ".",
                    "Already Processed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + status.toLowerCase() + " this leave request?",
                "Confirm Action",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if ("APPROVED".equals(status)) {
            service.approveLeave(leaveId);
        } else {
            service.rejectLeave(leaveId);
        }

        JOptionPane.showMessageDialog(this,
                "Leave request " + status.toLowerCase() + " successfully.");

        refreshTable();
    }
}