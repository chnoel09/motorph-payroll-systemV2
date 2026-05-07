package com.mycompany.oop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.oop.DatabaseConnection;
import com.mycompany.oop.model.PayrollHistoryRecord;

public class PayrollHistoryDatabaseRepository implements PayrollHistoryRepository {

    @Override
    public void savePayrollRecord(PayrollHistoryRecord record) {
        String sql = """
            INSERT INTO payroll_history (
                employee_id,
                cutoff_period,
                basic_component,
                allowance_component,
                gross,
                sss,
                philhealth,
                pagibig,
                tax,
                total_deductions,
                net
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, record.getEmployeeId());
            stmt.setString(2, record.getCutoffPeriod());
            stmt.setDouble(3, record.getBasicComponent());
            stmt.setDouble(4, record.getAllowanceComponent());
            stmt.setDouble(5, record.getGross());
            stmt.setDouble(6, record.getSss());
            stmt.setDouble(7, record.getPhilhealth());
            stmt.setDouble(8, record.getPagibig());
            stmt.setDouble(9, record.getTax());
            stmt.setDouble(10, record.getTotalDeductions());
            stmt.setDouble(11, record.getNet());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PayrollHistoryRecord> findAll() {
        List<PayrollHistoryRecord> records = new ArrayList<>();

        String sql = """
            SELECT employee_id, cutoff_period, basic_component, allowance_component,
                   gross, sss, philhealth, pagibig, tax, total_deductions, net
            FROM payroll_history
            ORDER BY cutoff_period, employee_id
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                records.add(mapRowToPayrollHistoryRecord(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public List<PayrollHistoryRecord> findByEmployeeId(int employeeId) {
        List<PayrollHistoryRecord> records = new ArrayList<>();

        String sql = """
            SELECT employee_id, cutoff_period, basic_component, allowance_component,
                   gross, sss, philhealth, pagibig, tax, total_deductions, net
            FROM payroll_history
            WHERE employee_id = ?
            ORDER BY cutoff_period
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRowToPayrollHistoryRecord(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public boolean existsByCutoff(String cutoffPeriod) {
        String sql = """
            SELECT COUNT(*) AS record_count
            FROM payroll_history
            WHERE LOWER(cutoff_period) = LOWER(?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cutoffPeriod);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("record_count") > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void deleteByCutoff(String cutoffPeriod) {
        String sql = """
            DELETE FROM payroll_history
            WHERE LOWER(cutoff_period) = LOWER(?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cutoffPeriod);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PayrollHistoryRecord mapRowToPayrollHistoryRecord(ResultSet rs) throws SQLException {
        return new PayrollHistoryRecord(
                rs.getInt("employee_id"),
                rs.getString("cutoff_period"),
                rs.getDouble("basic_component"),
                rs.getDouble("allowance_component"),
                rs.getDouble("gross"),
                rs.getDouble("sss"),
                rs.getDouble("philhealth"),
                rs.getDouble("pagibig"),
                rs.getDouble("tax"),
                rs.getDouble("total_deductions"),
                rs.getDouble("net")
        );
    }
}