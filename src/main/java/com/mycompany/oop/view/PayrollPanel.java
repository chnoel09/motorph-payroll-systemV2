/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.PayrollRecord;
import com.mycompany.oop.service.PayrollService;

public class PayrollPanel extends JPanel {

    private PayrollService payrollService;
    private JTable table;
    private JComboBox<String> cutoffBox;
    private JPanel summaryWrapper;
    private JButton processBtn;
    private JButton refreshBtn;
    private JLabel emptyStateLabel;
    private JScrollPane tableScrollPane;

    public PayrollPanel() {

        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Payroll Processing Center"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        summaryWrapper = new JPanel(new BorderLayout());
        summaryWrapper.setBackground(UITheme.BG);

        emptyStateLabel = new JLabel(
                "<html><div style='text-align:center;'>"
                + "No attendance cutoff available yet.<br>"
                + "Payroll preview will appear once attendance records exist."
                + "</div></html>",
                SwingConstants.CENTER
        );
        emptyStateLabel.setFont(UITheme.FONT_BODY_BOLD);
        emptyStateLabel.setForeground(UITheme.TEXT_SECONDARY);
        emptyStateLabel.setBorder(new EmptyBorder(40, 0, 40, 0));
        emptyStateLabel.setVisible(false);

        content.add(summaryWrapper, BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(emptyStateLabel, BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        if (cutoffBox.getItemCount() > 0) {
            updateEmptyState(false);
            refreshAll();
        } else {
            updateEmptyState(true);
        }
    }

    // ================= SUMMARY =================

    private JPanel createSummaryPanel(PayrollSummaryData summary) {

        NumberFormat peso = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        Color[] accents = {
                new Color(210, 43, 43), new Color(37, 99, 195),
                new Color(34, 160, 70), new Color(59, 130, 246),
                new Color(185, 30, 30), new Color(37, 99, 195),
                new Color(130, 80, 210), new Color(210, 43, 43),
        };

        String[][] cards = {
                {"Total Gross", peso.format(summary.totalGross)},
                {"Total Deductions", peso.format(summary.totalDeductions)},
                {"Total Net Payroll", peso.format(summary.totalNet)},
                {"Employees Paid", String.valueOf(summary.employeeCount)},
                {"Total SSS", peso.format(summary.totalSSS)},
                {"Total PhilHealth", peso.format(summary.totalPhilhealth)},
                {"Total Pag-IBIG", peso.format(summary.totalPagibig)},
                {"Total Tax", peso.format(summary.totalTax)},
        };

        JPanel panel = new JPanel(new GridLayout(2, 4, 14, 14));
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        for (int i = 0; i < cards.length; i++) {
            panel.add(createMetricCard(cards[i][0], cards[i][1], accents[i]));
        }

        return panel;
    }

    private JPanel createMetricCard(String title, String value, Color accent) {

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
                new EmptyBorder(14, 16, 12, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_CARD_LABEL);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= TABLE =================

    private JScrollPane createTablePanel() {
        table = new JTable();
        UITheme.styleTable(table);
        tableScrollPane = UITheme.createTableScrollPane(table);
        return tableScrollPane;
    }

    private void updateTable(Object[][] data) {
        String[] cols = {"Name", "Hours", "Gross", "Deductions", "Net"};

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    // ================= BUTTONS =================

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        List<String> cutoffs = payrollService.getAvailableCutoffs();
        cutoffBox = new JComboBox<>(cutoffs.toArray(new String[0]));

        processBtn = UITheme.createAccentButton("Process Payroll");
        refreshBtn = UITheme.createButton("Refresh");

        processBtn.setPreferredSize(new Dimension(160, 34));
        refreshBtn.setPreferredSize(new Dimension(100, 34));

        processBtn.addActionListener(e -> processPayroll());
        refreshBtn.addActionListener(e -> refreshAll());
        cutoffBox.addActionListener(e -> refreshAll());

        JLabel cutoffLabel = new JLabel("Cutoff:");
        cutoffLabel.setFont(UITheme.FONT_BODY);

        panel.add(cutoffLabel);
        panel.add(cutoffBox);
        panel.add(refreshBtn);
        panel.add(processBtn);

        return panel;
    }

    private void processPayroll() {
        if (cutoffBox.getSelectedItem() == null) return;

        String cutoff = cutoffBox.getSelectedItem().toString();
        setControlsEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return payrollService.processAndSavePayroll(cutoff, false);
            }

            @Override
            protected void done() {
                setControlsEnabled(true);
                try {
                    boolean success = get();

                    if (success) {
                        JOptionPane.showMessageDialog(PayrollPanel.this,
                                "Payroll processed successfully for " + cutoff + ".");
                        refreshAll();
                    } else {
                        JOptionPane.showMessageDialog(PayrollPanel.this,
                                "This cutoff has already been processed.",
                                "Duplicate Cutoff",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PayrollPanel.this,
                            "An error occurred while processing payroll.",
                            "Processing Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    // ================= REFRESH ALL =================

    private void refreshAll() {
        if (cutoffBox.getItemCount() == 0 || cutoffBox.getSelectedItem() == null) {
            updateEmptyState(true);
            return;
        }

        updateEmptyState(false);

        String cutoff = cutoffBox.getSelectedItem().toString();
        setControlsEnabled(false);

        SwingWorker<PayrollViewData, Void> worker = new SwingWorker<>() {
            @Override
            protected PayrollViewData doInBackground() {
                return buildPayrollViewData(cutoff);
            }

            @Override
            protected void done() {
                setControlsEnabled(true);

                try {
                    PayrollViewData viewData = get();

                    updateTable(viewData.tableData);

                    summaryWrapper.removeAll();
                    summaryWrapper.add(createSummaryPanel(viewData.summary), BorderLayout.CENTER);
                    summaryWrapper.revalidate();
                    summaryWrapper.repaint();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PayrollPanel.this,
                            "An error occurred while loading payroll data.",
                            "Load Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private PayrollViewData buildPayrollViewData(String cutoffPeriod) {
        List<Employee> list = payrollService.getEmployees();
        NumberFormat peso = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        Object[][] data = new Object[list.size()][5];
        PayrollSummaryData summary = new PayrollSummaryData();

        for (int i = 0; i < list.size(); i++) {
            Employee e = list.get(i);

            double hours = payrollService.getHoursForCutoff(e.getEmployeeId(), cutoffPeriod);
            PayrollRecord record = payrollService.processPayrollForEmployee(e, hours);

            data[i][0] = e.getFirstName() + " " + e.getLastName();
            data[i][1] = String.format("%.1f", hours);
            data[i][2] = peso.format(record.getGross());
            data[i][3] = peso.format(record.getTotalDeductions());
            data[i][4] = peso.format(record.getNet());

            summary.employeeCount++;
            summary.totalGross += record.getGross();
            summary.totalDeductions += record.getTotalDeductions();
            summary.totalNet += record.getNet();
            summary.totalSSS += record.getSss();
            summary.totalPhilhealth += record.getPhilhealth();
            summary.totalPagibig += record.getPagibig();
            summary.totalTax += record.getTax();
        }

        return new PayrollViewData(data, summary);
    }

    private void setControlsEnabled(boolean enabled) {
        if (cutoffBox != null) cutoffBox.setEnabled(enabled);
        if (refreshBtn != null) refreshBtn.setEnabled(enabled);
        if (processBtn != null) processBtn.setEnabled(enabled);
    }

    private void updateEmptyState(boolean showEmpty) {
        emptyStateLabel.setVisible(showEmpty);

        if (tableScrollPane != null) {
            tableScrollPane.setVisible(!showEmpty);
        }

        summaryWrapper.setVisible(!showEmpty);

        if (refreshBtn != null) refreshBtn.setEnabled(!showEmpty);
        if (processBtn != null) processBtn.setEnabled(!showEmpty);
    }

    // ================= HELPER DATA CLASSES =================

    private static class PayrollViewData {
        Object[][] tableData;
        PayrollSummaryData summary;

        PayrollViewData(Object[][] tableData, PayrollSummaryData summary) {
            this.tableData = tableData;
            this.summary = summary;
        }
    }

    private static class PayrollSummaryData {
        double totalGross = 0;
        double totalDeductions = 0;
        double totalNet = 0;
        int employeeCount = 0;
        double totalSSS = 0;
        double totalPhilhealth = 0;
        double totalPagibig = 0;
        double totalTax = 0;
    }
}

/*
PAYROLL PANEL – PERFORMANCE AND EMPTY STATE IMPROVEMENT

Enhancement Summary:
This panel was improved in response to MS2 feedback regarding payroll
loading latency and user experience when no attendance-based cutoff
is yet available.

Key Improvements:
• Payroll computation and summary generation are executed in a background
  thread (SwingWorker) to prevent UI freezing during heavy processing.
• Table data and summary metrics are computed in a single pass to avoid
  redundant service calls.
• UI controls are temporarily disabled during processing to prevent
  duplicate actions.
• An empty-state message is shown when no attendance cutoff exists yet,
  preventing a confusing blank screen.

Impact:
• Faster and smoother user experience when switching cutoff periods
• Reduced perceived latency during payroll processing
• Better guidance when payroll preview is unavailable
• More efficient use of service layer computations

Note:
This optimization maintains separation of concerns by keeping business
logic within the PayrollService while improving UI responsiveness and
clarity at the view level.
*/