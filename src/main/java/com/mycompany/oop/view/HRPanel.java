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
import com.mycompany.oop.service.EmployeeService;

public class HRPanel extends JPanel {

    private EmployeeService service;
    private JTable table;
    private Employee loggedInUser;

    public HRPanel(Employee loggedInUser) {

        this.loggedInUser = loggedInUser;
        service = new EmployeeService();

        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(UITheme.createTitleBar("Employee Management"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(16, 20, 16, 20));

        content.add(createTable(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    // ================= TABLE =================

    private JScrollPane createTable() {

        table = new JTable();
        UITheme.styleTable(table);

        refreshTable();

        return UITheme.createTableScrollPane(table);
    }

    private void refreshTable() {

        SwingWorker<Object[][], Void> worker = new SwingWorker<>() {
            @Override
            protected Object[][] doInBackground() {

                List<Employee> list = service.getAllEmployees();

                NumberFormat peso = NumberFormat.getCurrencyInstance(
                        new Locale("en", "PH"));

                Object[][] data = new Object[list.size()][7];

                for (int i = 0; i < list.size(); i++) {
                    Employee e = list.get(i);

                    data[i][0] = e.getEmployeeId();
                    data[i][1] = e.getFirstName();
                    data[i][2] = e.getLastName();
                    data[i][3] = e.getPosition();
                    data[i][4] = e.getEmploymentStatus();
                    data[i][5] = peso.format(e.getBasicSalary());
                    data[i][6] = e.getRole();
                }

                return data;
            }

            @Override
            protected void done() {
                try {
                    String[] columns = {
                            "ID", "First Name", "Last Name",
                            "Position", "Status", "Basic Salary", "Role"
                    };

                    DefaultTableModel model = new DefaultTableModel(get(), columns) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    table.setModel(model);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(HRPanel.this,
                            "Failed to load employee data.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    // ================= BUTTONS =================

    private JPanel createButtonPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        panel.setBackground(UITheme.BG);

        JButton viewBtn = UITheme.createButton("View");
        JButton addBtn = UITheme.createButton("Add");
        JButton editBtn = UITheme.createButton("Edit");
        JButton deleteBtn = UITheme.createCrudDangerButton("Delete");

        viewBtn.setPreferredSize(new Dimension(90, 34));
        addBtn.setPreferredSize(new Dimension(90, 34));
        editBtn.setPreferredSize(new Dimension(90, 34));
        deleteBtn.setPreferredSize(new Dimension(90, 34));

        viewBtn.addActionListener(e -> viewSelected());
        addBtn.addActionListener(e -> openEmployeeFormDialog(null, false));
        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());

        panel.add(viewBtn);
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        return panel;
    }

    // ================= ACTIONS =================

   private void viewSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to view.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        Employee emp = service.findById(id);
        
        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Employee record could not be found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
            refreshTable();
            return;
}        

        openEmployeeFormDialog(emp, true);
    }
    
    private void editSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        Employee emp = service.findById(id);

        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Employee record could not be found.",
                    "Not Found",
                    JOptionPane.ERROR_MESSAGE);
        refreshTable();
        return;
}
        
        openEmployeeFormDialog(emp, false);
    }

    private void deleteSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this employee and all related attendance, payroll, and leave records?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteEmployee(id);
                refreshTable();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete employee. Please try again or check the database connection.",
                        "Delete Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEmployeeFormDialog(Employee emp, boolean readOnly) {

       JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

       boolean isAdmin = loggedInUser != null &&
               "admin".equalsIgnoreCase(loggedInUser.getRole());

       EmployeeFormDialog dialog = new EmployeeFormDialog(
               parent, service, emp, isAdmin, readOnly
       );

       dialog.setLocationRelativeTo(this);
       dialog.setVisible(true);

       if (!readOnly) {
           refreshTable();
       }
   }
}

/*
HR PANEL – EMPLOYEE RECORD VIEW SUPPORT

Enhancement Summary:
This panel was extended to support full employee record viewing in
addition to add, edit, and delete operations.

Key Improvements:
• A view action can be used to open the complete employee profile.
• The table remains concise while full employee details stay accessible.
• CRUD actions continue to refresh the table after updates.

Impact:
• Better usability for reviewing complete employee data
• Cleaner table presentation without overcrowding columns
• More complete employee management workflow
*/
