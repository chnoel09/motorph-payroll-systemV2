/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;
import com.mycompany.oop.service.EmployeeService;
import com.mycompany.oop.util.PasswordUtil;

public class EmployeeFormDialog extends JDialog {

    private JTextField idField, firstNameField, lastNameField, positionField;
    private JTextField statusField, basicSalaryField, allowanceField, hourlyRateField;

    private JTextField birthdayField, addressField, phoneNumberField, emailField, supervisorField;
    private JTextField riceSubsidyField, phoneAllowanceField, clothingAllowanceField, grossSemiMonthlyRateField;
    private JTextField sssField, philhealthField, tinField, pagibigField;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    private EmployeeService service;
    private boolean isAdmin;
    private boolean readOnly;
    private Employee existingEmployee;

    public EmployeeFormDialog(JFrame parent,
                              EmployeeService service,
                              Employee employee,
                              boolean isAdmin,
                              boolean readOnly) {

        super(parent, true);
        this.service = service;
        this.isAdmin = isAdmin;
        this.readOnly = readOnly;
        this.existingEmployee = employee;

        String title;
        if (readOnly) {
            title = "View Employee";
        } else if (employee == null) {
            title = "Add Employee";
        } else {
            title = "Edit Employee";
        }

        setTitle(title);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG);

        add(UITheme.createTitleBar(getTitle()), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UITheme.BG);
        content.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        idField = createField("Employee ID:", formPanel);
        firstNameField = createField("First Name:", formPanel);
        lastNameField = createField("Last Name:", formPanel);
        birthdayField = createField("Birthday (YYYY-MM-DD):", formPanel);
        addressField = createField("Address:", formPanel);
        phoneNumberField = createField("Phone Number:", formPanel);
        emailField = createField("Email:", formPanel);
        positionField = createField("Position:", formPanel);
        statusField = createField("Employment Status:", formPanel);
        supervisorField = createField("Immediate Supervisor:", formPanel);

        basicSalaryField = createField("Basic Salary:", formPanel);
        riceSubsidyField = createField("Rice Subsidy:", formPanel);
        phoneAllowanceField = createField("Phone Allowance:", formPanel);
        clothingAllowanceField = createField("Clothing Allowance:", formPanel);

        allowanceField = createField("Total Allowance:", formPanel);
        allowanceField.setEditable(false);
        allowanceField.setBackground(new Color(235, 235, 235));

        grossSemiMonthlyRateField = createField("Gross Semi-Monthly Rate:", formPanel);
        grossSemiMonthlyRateField.setEditable(false);
        grossSemiMonthlyRateField.setBackground(new Color(235, 235, 235));

        hourlyRateField = createField("Hourly Rate:", formPanel);

        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateComputedFields();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateComputedFields();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateComputedFields();
            }
        };

        riceSubsidyField.getDocument().addDocumentListener(listener);
        phoneAllowanceField.getDocument().addDocumentListener(listener);
        clothingAllowanceField.getDocument().addDocumentListener(listener);
        basicSalaryField.getDocument().addDocumentListener(listener);

        sssField = createField("SSS Number:", formPanel);
        philhealthField = createField("PhilHealth Number:", formPanel);
        tinField = createField("TIN Number:", formPanel);
        pagibigField = createField("Pag-IBIG Number:", formPanel);

        if (isAdmin) {
            usernameField = createField("Username:", formPanel);

            formPanel.add(createLabel("Password:"));
            passwordField = new JPasswordField();
            styleField(passwordField);
            formPanel.add(passwordField);

            formPanel.add(createLabel("Role:"));
            roleBox = new JComboBox<>(new String[]{
                    "Admin", "HR", "Finance", "Employee", "IT"
            });
            roleBox.setFont(UITheme.FONT_BODY);
            roleBox.setBackground(Color.WHITE);
            formPanel.add(roleBox);
        }

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        content.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(UITheme.BG);

        JButton cancelBtn = UITheme.createFormButton(readOnly ? "Close" : "Cancel");
        JButton saveBtn = UITheme.createAccentButton("Save");

        cancelBtn.setPreferredSize(new Dimension(90, 34));
        saveBtn.setPreferredSize(new Dimension(90, 34));

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveEmployee());

        buttonPanel.add(cancelBtn);

        if (!readOnly) {
            buttonPanel.add(saveBtn);
        }

        content.add(buttonPanel, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        if (employee != null) {
            populateFields(employee);
            updateComputedFields();
            idField.setEditable(false);
            idField.setBackground(new Color(235, 235, 235));
        } else {
            idField.setText("Auto-generated");
            idField.setEditable(false);
            idField.setBackground(new Color(235, 235, 235));
            updateComputedFields();
        }

        if (readOnly) {
            setFieldsEditable(false);
        }

        setSize(720, 650);
        setLocationRelativeTo(parent);
    }

    private void setFieldsEditable(boolean editable) {
        idField.setEditable(false);
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        birthdayField.setEditable(editable);
        addressField.setEditable(editable);
        phoneNumberField.setEditable(editable);
        emailField.setEditable(editable);
        positionField.setEditable(editable);
        statusField.setEditable(editable);
        supervisorField.setEditable(editable);
        basicSalaryField.setEditable(editable);
        riceSubsidyField.setEditable(editable);
        phoneAllowanceField.setEditable(editable);
        clothingAllowanceField.setEditable(editable);
        hourlyRateField.setEditable(editable);
        sssField.setEditable(editable);
        philhealthField.setEditable(editable);
        tinField.setEditable(editable);
        pagibigField.setEditable(editable);

        allowanceField.setEditable(false);
        grossSemiMonthlyRateField.setEditable(false);

        if (usernameField != null) usernameField.setEditable(editable);
        if (passwordField != null) passwordField.setEditable(editable);
        if (roleBox != null) roleBox.setEnabled(editable);

        applyReadOnlyStyle(firstNameField, editable);
        applyReadOnlyStyle(lastNameField, editable);
        applyReadOnlyStyle(birthdayField, editable);
        applyReadOnlyStyle(addressField, editable);
        applyReadOnlyStyle(phoneNumberField, editable);
        applyReadOnlyStyle(emailField, editable);
        applyReadOnlyStyle(positionField, editable);
        applyReadOnlyStyle(statusField, editable);
        applyReadOnlyStyle(supervisorField, editable);
        applyReadOnlyStyle(basicSalaryField, editable);
        applyReadOnlyStyle(riceSubsidyField, editable);
        applyReadOnlyStyle(phoneAllowanceField, editable);
        applyReadOnlyStyle(clothingAllowanceField, editable);
        applyReadOnlyStyle(hourlyRateField, editable);
        applyReadOnlyStyle(sssField, editable);
        applyReadOnlyStyle(philhealthField, editable);
        applyReadOnlyStyle(tinField, editable);
        applyReadOnlyStyle(pagibigField, editable);

        if (usernameField != null) applyReadOnlyStyle(usernameField, editable);
        if (passwordField != null) {
            passwordField.setBackground(editable ? Color.WHITE : new Color(235, 235, 235));
        }
    }

    private void applyReadOnlyStyle(JTextField field, boolean editable) {
        field.setBackground(editable ? Color.WHITE : new Color(235, 235, 235));
    }

    private void updateComputedFields() {
        try {
            double rice = parseDoubleSafe(riceSubsidyField.getText());
            double phone = parseDoubleSafe(phoneAllowanceField.getText());
            double clothing = parseDoubleSafe(clothingAllowanceField.getText());
            double basicSalary = parseDoubleSafe(basicSalaryField.getText());

            double totalAllowance = rice + phone + clothing;
            double grossSemiMonthly = (basicSalary + totalAllowance) / 2.0;

            allowanceField.setText(String.valueOf(totalAllowance));
            grossSemiMonthlyRateField.setText(String.valueOf(grossSemiMonthly));

        } catch (Exception e) {
            allowanceField.setText("0");
            grossSemiMonthlyRateField.setText("0");
        }
    }

    private double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private JTextField createField(String labelText, JPanel panel) {
        panel.add(createLabel(labelText));
        JTextField field = new JTextField();
        styleField(field);
        panel.add(field);
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BODY_BOLD);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(180, 30));
        field.setFont(UITheme.FONT_BODY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void populateFields(Employee e) {
        idField.setText(String.valueOf(e.getEmployeeId()));
        firstNameField.setText(e.getFirstName());
        lastNameField.setText(e.getLastName());
        birthdayField.setText(e.getBirthday());
        addressField.setText(e.getAddress());
        phoneNumberField.setText(e.getPhoneNumber());
        emailField.setText(e.getEmail());
        positionField.setText(e.getPosition());
        statusField.setText(e.getEmploymentStatus());
        supervisorField.setText(e.getImmediateSupervisor());

        basicSalaryField.setText(String.valueOf(e.getBasicSalary()));
        riceSubsidyField.setText(String.valueOf(e.getRiceSubsidy()));
        phoneAllowanceField.setText(String.valueOf(e.getPhoneAllowance()));
        clothingAllowanceField.setText(String.valueOf(e.getClothingAllowance()));
        allowanceField.setText(String.valueOf(e.getAllowance()));
        grossSemiMonthlyRateField.setText(String.valueOf(e.getGrossSemiMonthlyRate()));
        hourlyRateField.setText(String.valueOf(e.getHourlyRate()));

        sssField.setText(e.getSssNumber());
        philhealthField.setText(e.getPhilhealthNumber());
        tinField.setText(e.getTinNumber());
        pagibigField.setText(e.getPagibigNumber());

        if (isAdmin) {
            usernameField.setText(e.getUsername());
            passwordField.setText("");
            roleBox.setSelectedItem(e.getRole());
        }
    }

    private void saveEmployee() {

        if (readOnly) {
            dispose();
            return;
        }

        if (firstNameField.getText().trim().isEmpty()
                || lastNameField.getText().trim().isEmpty()
                || birthdayField.getText().trim().isEmpty()
                || addressField.getText().trim().isEmpty()
                || phoneNumberField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()
                || positionField.getText().trim().isEmpty()
                || statusField.getText().trim().isEmpty()
                || supervisorField.getText().trim().isEmpty()
                || basicSalaryField.getText().trim().isEmpty()
                || riceSubsidyField.getText().trim().isEmpty()
                || phoneAllowanceField.getText().trim().isEmpty()
                || clothingAllowanceField.getText().trim().isEmpty()
                || allowanceField.getText().trim().isEmpty()
                || grossSemiMonthlyRateField.getText().trim().isEmpty()
                || hourlyRateField.getText().trim().isEmpty()
                || sssField.getText().trim().isEmpty()
                || philhealthField.getText().trim().isEmpty()
                || tinField.getText().trim().isEmpty()
                || pagibigField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isAdmin) {
            if (usernameField.getText().trim().isEmpty()
                    || (existingEmployee == null && new String(passwordField.getPassword()).trim().isEmpty())
                    || roleBox.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Please complete username, password, and role.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String email = emailField.getText().trim();
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String phoneNumber = phoneNumberField.getText().trim();
        if (!phoneNumber.matches("^63\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "Phone number must start with 63 and contain exactly 12 digits.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!sssField.getText().trim().matches("\\d+")
                || !philhealthField.getText().trim().matches("\\d+")
                || !tinField.getText().trim().matches("\\d+")
                || !pagibigField.getText().trim().matches("\\d+")) {

            JOptionPane.showMessageDialog(this,
                    "Government ID fields must contain digits only.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int employeeId = existingEmployee == null
                    ? 0
                    : Integer.parseInt(idField.getText().trim());

            double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
            double riceSubsidy = Double.parseDouble(riceSubsidyField.getText().trim());
            double phoneAllowance = Double.parseDouble(phoneAllowanceField.getText().trim());
            double clothingAllowance = Double.parseDouble(clothingAllowanceField.getText().trim());
            double allowance = parseDoubleSafe(allowanceField.getText());
            double grossSemiMonthlyRate = parseDoubleSafe(grossSemiMonthlyRateField.getText());
            double hourlyRate = Double.parseDouble(hourlyRateField.getText().trim());

            if (basicSalary < 0 || riceSubsidy < 0 || phoneAllowance < 0
                    || clothingAllowance < 0 || allowance < 0
                    || grossSemiMonthlyRate < 0 || hourlyRate < 0) {

                JOptionPane.showMessageDialog(this,
                        "Salary values cannot be negative.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (basicSalary > 1_000_000) {
                JOptionPane.showMessageDialog(this,
                        "Basic salary exceeds the allowed maximum.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String username;
            String password;
            String role;

            if (isAdmin) {
                username = usernameField.getText().trim();
                String inputPassword = new String(passwordField.getPassword()).trim();

                if (existingEmployee != null && inputPassword.isEmpty()) {
                    password = existingEmployee.getPassword();
                } else {
                    password = PasswordUtil.hash(inputPassword);
                }

                role = roleBox.getSelectedItem().toString();

            } else if (existingEmployee != null) {
                username = existingEmployee.getUsername();
                password = existingEmployee.getPassword();
                role = existingEmployee.getRole();

            } else {
                String first = firstNameField.getText().trim().toLowerCase().replace(" ", "");
                String last = lastNameField.getText().trim().toLowerCase().replace(" ", "");

                String lastInitial = last.isEmpty() ? "x" : String.valueOf(last.charAt(0));
                username = first + "." + lastInitial;

                password = PasswordUtil.hash("1234");
                role = "Employee";
            }

            Employee newEmployee = new RegularEmployee(
                    employeeId,
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    positionField.getText().trim(),
                    statusField.getText().trim(),
                    basicSalary,
                    allowance,
                    hourlyRate,
                    username,
                    password,
                    role
            );

            newEmployee.setBirthday(birthdayField.getText().trim());
            newEmployee.setAddress(addressField.getText().trim());
            newEmployee.setPhoneNumber(phoneNumber);
            newEmployee.setEmail(email);

            String supervisor = supervisorField.getText().trim();
            newEmployee.setImmediateSupervisor(
                    supervisor.isEmpty() ? "N/A" : supervisor
            );

            newEmployee.setRiceSubsidy(riceSubsidy);
            newEmployee.setPhoneAllowance(phoneAllowance);
            newEmployee.setClothingAllowance(clothingAllowance);
            newEmployee.setGrossSemiMonthlyRate(grossSemiMonthlyRate);

            newEmployee.setSssNumber(sssField.getText().trim());
            newEmployee.setPhilhealthNumber(philhealthField.getText().trim());
            newEmployee.setTinNumber(tinField.getText().trim());
            newEmployee.setPagibigNumber(pagibigField.getText().trim());
            
            if (existingEmployee == null) {
                service.addEmployee(newEmployee);

                JOptionPane.showMessageDialog(
                        this,
                        "Employee added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                service.updateEmployee(newEmployee);

                JOptionPane.showMessageDialog(
                        this,
                        "Employee updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Numeric fields must contain valid numbers.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input values.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

/*
EMPLOYEE FORM DIALOG – VIEW, ADD, EDIT, AND VALIDATION SUPPORT

Enhancement Summary:
This dialog was extended to support complete employee record viewing
in addition to add and edit operations.

Key Improvements:
• Supports read-only mode for viewing full employee details
• Preserves role-based behavior for Admin and non-Admin users
• Automatically computes allowance and semi-monthly gross values
• Enforces validation for required fields and government IDs

Impact:
• Completes the employee management workflow
• Improves usability without overcrowding the employee table
• Keeps data entry and record review within a single reusable dialog
*/
