package com.mycompany.oop.service;

import java.util.List;

import com.mycompany.oop.model.Employee;
import com.mycompany.oop.repository.EmployeeDatabaseRepository;
import com.mycompany.oop.repository.EmployeeRepository;
import com.mycompany.oop.util.PasswordUtil;

public class LoginService {

    private EmployeeRepository repository;

    public LoginService() {
        this.repository = new EmployeeDatabaseRepository();
    }

    // Default login (no role restriction)
    public Employee login(String username, String password) {
        return login(username, password, null);
    }

    // Login with optional role validation
    public Employee login(String username, String password, String requiredRole) {

        if (username == null || password == null) {
            return null;
        }

        username = username.trim().toLowerCase();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) {
            return null;
        }

        List<Employee> employees = repository.getAllEmployees();

        for (Employee emp : employees) {

            String storedUsername = emp.getUsername();
            String storedPassword = emp.getPassword();
            String storedRole = emp.getRole();

            String hashedInputPassword = PasswordUtil.hash(password);

                boolean credentialsMatch =
                        storedUsername.equalsIgnoreCase(username)
                        && storedPassword.equals(hashedInputPassword);

            if (!credentialsMatch) {
                continue;
            }

            if (requiredRole == null) {
                return emp;
            }

            if (storedRole != null && storedRole.equalsIgnoreCase(requiredRole)) {
                return emp;
            }

            return null;
        }

        return null;
    }
}