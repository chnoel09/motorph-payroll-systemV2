package com.mycompany.oop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.oop.DatabaseConnection;
import com.mycompany.oop.model.Employee;
import com.mycompany.oop.model.RegularEmployee;

public class EmployeeDatabaseRepository implements EmployeeRepository {

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        String sql = """
            SELECT 
                employee_id,
                first_name,
                last_name,
                birthday,
                address,
                phone_number,
                email,
                status,
                position,
                immediate_supervisor,
                basic_salary,
                rice_subsidy,
                phone_allowance,
                clothing_allowance,
                gross_semi_monthly_rate,
                hourly_rate,
                sss_number,
                philhealth_number,
                tin_number,
                pagibig_number,
                username,
                password,
                role
            FROM employees
            """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    @Override
    public Employee findEmployee(int employeeId) {
        String sql = """
            SELECT 
                employee_id,
                first_name,
                last_name,
                birthday,
                address,
                phone_number,
                email,
                status,
                position,
                immediate_supervisor,
                basic_salary,
                rice_subsidy,
                phone_allowance,
                clothing_allowance,
                gross_semi_monthly_rate,
                hourly_rate,
                sss_number,
                philhealth_number,
                tin_number,
                pagibig_number,
                username,
                password,
                role
            FROM employees
            WHERE employee_id = ?
            """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEmployee(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = """
            INSERT INTO employees (
                first_name,
                last_name,
                birthday,
                address,
                phone_number,
                email,
                status,
                position,
                immediate_supervisor,
                basic_salary,
                rice_subsidy,
                phone_allowance,
                clothing_allowance,
                gross_semi_monthly_rate,
                hourly_rate,
                sss_number,
                philhealth_number,
                tin_number,
                pagibig_number,
                username,
                password,
                role
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getBirthday());
            stmt.setString(4, employee.getAddress());
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setString(6, employee.getEmail());
            stmt.setString(7, employee.getEmploymentStatus());
            stmt.setString(8, employee.getPosition());
            stmt.setString(9, employee.getImmediateSupervisor());
            stmt.setDouble(10, employee.getBasicSalary());
            stmt.setDouble(11, employee.getRiceSubsidy());
            stmt.setDouble(12, employee.getPhoneAllowance());
            stmt.setDouble(13, employee.getClothingAllowance());
            stmt.setDouble(14, employee.getGrossSemiMonthlyRate());
            stmt.setDouble(15, employee.getHourlyRate());
            stmt.setString(16, employee.getSssNumber());
            stmt.setString(17, employee.getPhilhealthNumber());
            stmt.setString(18, employee.getTinNumber());
            stmt.setString(19, employee.getPagibigNumber());
            stmt.setString(20, employee.getUsername());
            stmt.setString(21, employee.getPassword());
            stmt.setString(22, employee.getRole());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add employee.", e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        String sql = """
            UPDATE employees SET
                first_name = ?,
                last_name = ?,
                birthday = ?,
                address = ?,
                phone_number = ?,
                email = ?,
                status = ?,
                position = ?,
                immediate_supervisor = ?,
                basic_salary = ?,
                rice_subsidy = ?,
                phone_allowance = ?,
                clothing_allowance = ?,
                gross_semi_monthly_rate = ?,
                hourly_rate = ?,
                sss_number = ?,
                philhealth_number = ?,
                tin_number = ?,
                pagibig_number = ?,
                username = ?,
                password = ?,
                role = ?
            WHERE employee_id = ?
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getBirthday());
            stmt.setString(4, employee.getAddress());
            stmt.setString(5, employee.getPhoneNumber());
            stmt.setString(6, employee.getEmail());
            stmt.setString(7, employee.getEmploymentStatus());
            stmt.setString(8, employee.getPosition());
            stmt.setString(9, employee.getImmediateSupervisor());
            stmt.setDouble(10, employee.getBasicSalary());
            stmt.setDouble(11, employee.getRiceSubsidy());
            stmt.setDouble(12, employee.getPhoneAllowance());
            stmt.setDouble(13, employee.getClothingAllowance());
            stmt.setDouble(14, employee.getGrossSemiMonthlyRate());
            stmt.setDouble(15, employee.getHourlyRate());
            stmt.setString(16, employee.getSssNumber());
            stmt.setString(17, employee.getPhilhealthNumber());
            stmt.setString(18, employee.getTinNumber());
            stmt.setString(19, employee.getPagibigNumber());
            stmt.setString(20, employee.getUsername());
            stmt.setString(21, employee.getPassword());
            stmt.setString(22, employee.getRole());
            stmt.setInt(23, employee.getEmployeeId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update employee.", e);
        }
    }

    @Override
    public void deleteEmployee(int employeeId) {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null) {
                throw new SQLException("Database connection is not available.");
            }

            conn.setAutoCommit(false);

            try {
                deleteEmployeeRows(conn, "DELETE FROM attendance_records WHERE employee_id = ?", employeeId);
                deleteEmployeeRows(conn, "DELETE FROM payroll_history WHERE employee_id = ?", employeeId);
                deleteEmployeeRows(conn, "DELETE FROM payroll_records WHERE employee_id = ?", employeeId);
                deleteEmployeeRows(conn, "DELETE FROM leaves WHERE employee_id = ?", employeeId);
                deleteEmployeeRows(conn, "DELETE FROM employees WHERE employee_id = ?", employeeId);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete employee.", e);
        }
    }

    private void deleteEmployeeRows(Connection conn, String sql, int employeeId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.executeUpdate();
        }
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {

        double totalAllowance =
                rs.getDouble("rice_subsidy")
              + rs.getDouble("phone_allowance")
              + rs.getDouble("clothing_allowance");

        RegularEmployee employee = new RegularEmployee(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("position"),
                rs.getString("status"),
                rs.getDouble("basic_salary"),
                totalAllowance,
                rs.getDouble("hourly_rate"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role")
        );

        employee.setBirthday(rs.getString("birthday"));
        employee.setAddress(rs.getString("address"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setEmail(rs.getString("email"));
        employee.setImmediateSupervisor(rs.getString("immediate_supervisor"));

        employee.setRiceSubsidy(rs.getDouble("rice_subsidy"));
        employee.setPhoneAllowance(rs.getDouble("phone_allowance"));
        employee.setClothingAllowance(rs.getDouble("clothing_allowance"));
        employee.setGrossSemiMonthlyRate(rs.getDouble("gross_semi_monthly_rate"));

        employee.setSssNumber(rs.getString("sss_number"));
        employee.setPhilhealthNumber(rs.getString("philhealth_number"));
        employee.setTinNumber(rs.getString("tin_number"));
        employee.setPagibigNumber(rs.getString("pagibig_number"));

        return employee;
    }
}
