/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import com.mycompany.oop.model.PayrollHistoryRecord;
import com.mycompany.oop.service.PayrollService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HRPayrollHistoryPanel extends JPanel {

    private PayrollService payrollService;
    private JTable table;
    private JComboBox<String> cutoffBox;
    private JButton loadBtn;
    private JButton exportBtn;
    private JButton clearBtn;
    private JLabel emptyStateLabel;
    private JScrollPane tableScrollPane;

    private JPanel summaryPanel;
    private JLabel cutoffValueLabel;
    private JLabel recordsValueLabel;
    private JLabel grossValueLabel;
    private JLabel deductionsValueLabel;
    private JLabel netValueLabel;

    public HRPayrollHistoryPanel() {
        payrollService = new PayrollService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Payroll History (All Employees)"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        emptyStateLabel = new JLabel(
                "<html><div style='text-align:center;'>"
                + "No processed payroll history available yet.<br>"
                + "Process payroll first before viewing saved records."
                + "</div></html>",
                SwingConstants.CENTER
        );
        emptyStateLabel.setFont(UITheme.FONT_BODY_BOLD);
        emptyStateLabel.setForeground(UITheme.TEXT_SECONDARY);
        emptyStateLabel.setBorder(new EmptyBorder(40, 0, 40, 0));
        emptyStateLabel.setVisible(false);

        JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.setBackground(UITheme.BG);
        topPanel.add(createSummaryPanel(), BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UITheme.BG);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        centerPanel.add(emptyStateLabel, BorderLayout.NORTH);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);

        if (cutoffBox.getItemCount() > 0) {
            updateEmptyState(false);
            loadHistory();
        } else {
            clearSummary();
            updateEmptyState(true);
        }
    }

    private JPanel createSummaryPanel() {
        summaryPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        summaryPanel.setBackground(UITheme.BG);
        summaryPanel.setBorder(new EmptyBorder(0, 0, 14, 0));

        cutoffValueLabel = new JLabel("--");
        recordsValueLabel = new JLabel("0");
        grossValueLabel = new JLabel("₱0.00");
        deductionsValueLabel = new JLabel("₱0.00");
        netValueLabel = new JLabel("₱0.00");

        summaryPanel.add(createMetricCard("Selected Cutoff", cutoffValueLabel));
        summaryPanel.add(createMetricCard("Records Loaded", recordsValueLabel));
        summaryPanel.add(createMetricCard("Total Gross", grossValueLabel));
        summaryPanel.add(createMetricCard("Total Deductions", deductionsValueLabel));
        summaryPanel.add(createMetricCard("Total Net", netValueLabel));

        return summaryPanel;
    }

    private JPanel createMetricCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(14, 16, 12, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_CARD_LABEL);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setBorder(new EmptyBorder(6, 0, 0, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JScrollPane createTablePanel() {
        table = new JTable();
        UITheme.styleTable(table);
        tableScrollPane = UITheme.createTableScrollPane(table);
        return tableScrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        List<String> processedCutoffs = payrollService.getProcessedCutoffs();
        cutoffBox = new JComboBox<>(processedCutoffs.toArray(new String[0]));
        cutoffBox.addActionListener(e -> loadHistory());

        loadBtn = UITheme.createButton("Load");
        exportBtn = UITheme.createButton("Export CSV");
        clearBtn = UITheme.createSidebarDangerButton("Clear Cutoff");

        loadBtn.setPreferredSize(new Dimension(100, 34));
        exportBtn.setPreferredSize(new Dimension(120, 34));
        clearBtn.setPreferredSize(new Dimension(130, 34));

        loadBtn.addActionListener(e -> loadHistory());
        exportBtn.addActionListener(e -> exportHistory());
        clearBtn.addActionListener(e -> clearCutoff());

        JLabel cutoffLabel = new JLabel("Cutoff:");
        cutoffLabel.setFont(UITheme.FONT_BODY);

        panel.add(cutoffLabel);
        panel.add(cutoffBox);
        panel.add(loadBtn);
        panel.add(exportBtn);
        panel.add(clearBtn);

        return panel;
    }

    private void loadHistory() {
        if (cutoffBox.getItemCount() == 0 || cutoffBox.getSelectedItem() == null) {
            clearSummary();
            updateEmptyState(true);
            return;
        }

        String cutoff = cutoffBox.getSelectedItem().toString();
        setControlsEnabled(false);

        SwingWorker<List<PayrollHistoryRecord>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PayrollHistoryRecord> doInBackground() {
                return payrollService.getPayrollHistoryByCutoff(cutoff);
            }

            @Override
            protected void done() {
                setControlsEnabled(true);

                try {
                    List<PayrollHistoryRecord> records = get();
                    updateTable(records);
                    updateSummary(cutoff, records);
                    updateEmptyState(records.isEmpty());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HRPayrollHistoryPanel.this,
                            "An error occurred while loading payroll history.",
                            "Load Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void updateTable(List<PayrollHistoryRecord> records) {
        NumberFormat peso = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        String[] cols = {
                "Emp ID", "Cutoff", "Gross", "SSS", "PhilHealth",
                "Pag-IBIG", "Tax", "Total Deductions", "Net"
        };

        Object[][] data = new Object[records.size()][9];

        for (int i = 0; i < records.size(); i++) {
            PayrollHistoryRecord record = records.get(i);

            data[i][0] = record.getEmployeeId();
            data[i][1] = record.getCutoffPeriod();
            data[i][2] = peso.format(record.getGross());
            data[i][3] = peso.format(record.getSss());
            data[i][4] = peso.format(record.getPhilhealth());
            data[i][5] = peso.format(record.getPagibig());
            data[i][6] = peso.format(record.getTax());
            data[i][7] = peso.format(record.getTotalDeductions());
            data[i][8] = peso.format(record.getNet());
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }

    private void updateSummary(String cutoff, List<PayrollHistoryRecord> records) {
        NumberFormat peso = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        double totalGross = 0.0;
        double totalDeductions = 0.0;
        double totalNet = 0.0;

        for (PayrollHistoryRecord record : records) {
            totalGross += record.getGross();
            totalDeductions += record.getTotalDeductions();
            totalNet += record.getNet();
        }

        cutoffValueLabel.setText(cutoff == null || cutoff.trim().isEmpty() ? "--" : cutoff);
        recordsValueLabel.setText(String.valueOf(records.size()));
        grossValueLabel.setText(peso.format(totalGross));
        deductionsValueLabel.setText(peso.format(totalDeductions));
        netValueLabel.setText(peso.format(totalNet));
    }

    private void clearSummary() {
        cutoffValueLabel.setText("--");
        recordsValueLabel.setText("0");
        grossValueLabel.setText("₱0.00");
        deductionsValueLabel.setText("₱0.00");
        netValueLabel.setText("₱0.00");
    }

    private void exportHistory() {
        if (cutoffBox.getSelectedItem() == null) {
            return;
        }

        String cutoff = cutoffBox.getSelectedItem().toString();

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("payroll_history_" + cutoff + ".csv"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = chooser.getSelectedFile().getAbsolutePath();

        boolean success = payrollService.exportPayrollHistoryByCutoff(cutoff, filePath);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Payroll history exported successfully.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No payroll history found for the selected cutoff.",
                    "Export Failed",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearCutoff() {
        if (cutoffBox.getSelectedItem() == null) {
            return;
        }

        String cutoff = cutoffBox.getSelectedItem().toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete all saved payroll history for " + cutoff + "?",
                "Confirm Clear Cutoff",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        payrollService.deleteCutoff(cutoff);

        cutoffBox.removeAllItems();
        List<String> processedCutoffs = payrollService.getProcessedCutoffs();
        for (String item : processedCutoffs) {
            cutoffBox.addItem(item);
        }

        if (cutoffBox.getItemCount() > 0) {
            updateEmptyState(false);
            loadHistory();
        } else {
            table.setModel(new DefaultTableModel());
            clearSummary();
            updateEmptyState(true);
        }

        JOptionPane.showMessageDialog(this,
                "Payroll history for " + cutoff + " has been cleared.");
    }

    private void setControlsEnabled(boolean enabled) {
        if (cutoffBox != null) cutoffBox.setEnabled(enabled);
        if (loadBtn != null) loadBtn.setEnabled(enabled);
        if (exportBtn != null) exportBtn.setEnabled(enabled);
        if (clearBtn != null) clearBtn.setEnabled(enabled);
    }

    private void updateEmptyState(boolean showEmpty) {
        emptyStateLabel.setVisible(showEmpty);

        if (tableScrollPane != null) {
            tableScrollPane.setVisible(!showEmpty);
        }

        if (summaryPanel != null) {
            summaryPanel.setVisible(true);
        }

        if (showEmpty) {
            clearSummary();
        }

        if (loadBtn != null) loadBtn.setEnabled(!showEmpty);
        if (exportBtn != null) exportBtn.setEnabled(!showEmpty);
        if (clearBtn != null) clearBtn.setEnabled(!showEmpty);
    }
}