/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import com.mycompany.oop.model.AttendanceRecord;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.service.AttendanceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AttendancePanel extends JPanel {

    private Employee employee;
    private AttendanceService attendanceService;

    private JLabel statusLabel;
    private JTable table;
    private JButton timeInBtn;
    private JButton timeOutBtn;

    private JComboBox<String> cutoffComboBox;
    private JButton applyFilterBtn;
    private JButton resetFilterBtn;

    public AttendancePanel(Employee employee) {
        this.employee = employee;
        this.attendanceService = new AttendanceService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("My Attendance"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createTodayPanel(), BorderLayout.NORTH);
        content.add(createHistorySection(), BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadCutoffOptions();
        refreshAttendance();
    }

    private JPanel createTodayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER),
                        new EmptyBorder(18, 22, 18, 22)
                )
        );

        JLabel title = new JLabel("Today's Attendance");
        title.setFont(UITheme.FONT_SECTION);
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        statusLabel = new JLabel("Not clocked in today");
        statusLabel.setFont(UITheme.FONT_BODY_BOLD);
        statusLabel.setForeground(UITheme.DANGER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttonPanel.setBackground(Color.WHITE);

        timeInBtn = UITheme.createAccentButton("Time In");
        timeOutBtn = UITheme.createButton("Time Out");

        timeInBtn.setPreferredSize(new Dimension(120, 36));
        timeOutBtn.setPreferredSize(new Dimension(120, 36));

        timeInBtn.addActionListener(e -> {
            attendanceService.timeIn(employee.getEmployeeId());
            loadCutoffOptions();
            refreshAttendance();
        });

        timeOutBtn.addActionListener(e -> {
            attendanceService.timeOut(employee.getEmployeeId());
            loadCutoffOptions();
            refreshAttendance();
        });

        buttonPanel.add(timeInBtn);
        buttonPanel.add(timeOutBtn);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(Color.WHITE);
        topSection.add(title, BorderLayout.NORTH);
        topSection.add(statusLabel, BorderLayout.CENTER);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(UITheme.BG);
        outerWrapper.setBorder(new EmptyBorder(0, 0, 16, 0));
        outerWrapper.add(panel, BorderLayout.CENTER);

        return outerWrapper;
    }

    private JPanel createHistorySection() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setBackground(UITheme.BG);

        wrapper.add(createFilterPanel(), BorderLayout.NORTH);
        wrapper.add(createHistoryPanel(), BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(UITheme.BG);

        JLabel cutoffLabel = new JLabel("Cutoff:");
        cutoffLabel.setFont(UITheme.FONT_BODY_BOLD);
        cutoffLabel.setForeground(UITheme.TEXT_PRIMARY);

        cutoffComboBox = new JComboBox<>();
        cutoffComboBox.setPreferredSize(new Dimension(180, 34));

        applyFilterBtn = UITheme.createAccentButton("Apply");
        resetFilterBtn = UITheme.createButton("Show All");

        applyFilterBtn.setPreferredSize(new Dimension(100, 34));
        resetFilterBtn.setPreferredSize(new Dimension(110, 34));

        applyFilterBtn.addActionListener(e -> refreshAttendance());
        resetFilterBtn.addActionListener(e -> {
            cutoffComboBox.setSelectedIndex(0);
            refreshAttendance();
        });

        panel.add(cutoffLabel);
        panel.add(cutoffComboBox);
        panel.add(applyFilterBtn);
        panel.add(resetFilterBtn);

        return panel;
    }

    private JScrollPane createHistoryPanel() {
        table = new JTable();
        UITheme.styleTable(table);

        return UITheme.createTableScrollPane(table);
    }

    private void loadCutoffOptions() {
        if (cutoffComboBox == null) {
            return;
        }

        cutoffComboBox.removeAllItems();
        cutoffComboBox.addItem("All Records");

        List<String> cutoffs = attendanceService.getAvailableCutoffsForEmployee(employee.getEmployeeId());
        for (String cutoff : cutoffs) {
            cutoffComboBox.addItem(cutoff);
        }
    }

    private void refreshAttendance() {
        refreshStatusSection();

        List<AttendanceRecord> history;
        String selectedCutoff = cutoffComboBox != null && cutoffComboBox.getSelectedItem() != null
                ? cutoffComboBox.getSelectedItem().toString()
                : "All Records";

        if ("All Records".equals(selectedCutoff)) {
            history = attendanceService.getAttendanceHistory(employee.getEmployeeId());
        } else {
            history = attendanceService.getAttendanceHistoryByCutoff(employee.getEmployeeId(), selectedCutoff);
        }

        String[] columns = {"Date", "Time In", "Time Out", "Hours Worked"};
        Object[][] data = new Object[history.size()][4];

        for (int i = 0; i < history.size(); i++) {
            AttendanceRecord record = history.get(i);
            data[i][0] = record.getDate();
            data[i][1] = record.getTimeIn();
            data[i][2] = (record.getTimeOut() == null || record.getTimeOut().isEmpty())
                    ? "--"
                    : record.getTimeOut();
            data[i][3] = String.format("%.2f", attendanceService.getHoursWorked(record));
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private void refreshStatusSection() {
        List<AttendanceRecord> fullHistory =
                attendanceService.getAttendanceHistory(employee.getEmployeeId());

        if (fullHistory.isEmpty()) {
            statusLabel.setText("Not clocked in today");
            statusLabel.setForeground(UITheme.DANGER);
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
            return;
        }

        AttendanceRecord latest = fullHistory.get(fullHistory.size() - 1);
        String todayStr = LocalDate.now().toString();

        if (latest.getDate().equals(todayStr)) {

            if (latest.getTimeIn() != null && !latest.getTimeIn().isEmpty()
                    && (latest.getTimeOut() == null || latest.getTimeOut().isEmpty())) {

                statusLabel.setText("Timed in today at " + latest.getTimeIn());
                statusLabel.setForeground(UITheme.SUCCESS);
                timeInBtn.setEnabled(false);
                timeOutBtn.setEnabled(true);

            } else if (latest.getTimeOut() != null && !latest.getTimeOut().isEmpty()) {

                statusLabel.setText("Last session completed today (" +
                        latest.getTimeIn() + " - " + latest.getTimeOut() + ")");
                statusLabel.setForeground(UITheme.TEXT_SECONDARY);
                timeInBtn.setEnabled(true);
                timeOutBtn.setEnabled(false);

            } else {
                statusLabel.setText("Not clocked in today");
                statusLabel.setForeground(UITheme.DANGER);
                timeInBtn.setEnabled(true);
                timeOutBtn.setEnabled(false);
            }

        } else {
            statusLabel.setText("Not clocked in today");
            statusLabel.setForeground(UITheme.DANGER);
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
        }
    }
}
