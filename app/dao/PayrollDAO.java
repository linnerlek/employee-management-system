package app.dao;

import app.db.DBConnection;
import app.model.PayStatement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    // gets most recent pay date for specific employee
    public static String getLastPayDate(int empid) {
        String query = "SELECT pay_date FROM payroll WHERE empid = ? ORDER BY pay_date DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("pay_date");
            } else {
                return "Not Available";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    // gets latest payroll date across all employees
    public static String getMostRecentPayrollDate() {
        String query = "SELECT MAX(pay_date) AS most_recent FROM payroll";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            if (rs.next()) {
                return rs.getString("most_recent");
            } else {
                return "Not Available";
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    
    // fetches pay history for single employee
    public static List<PayStatement> getPayStatements(int empid) {
        List<PayStatement> statements = new ArrayList<>();
    
        String query = """
            SELECT p.empid, CONCAT(e.fname, ' ', e.lname) AS name, p.pay_date,
                p.earnings, p.fed_tax, p.fed_med, p.fed_SS, p.state_tax, 
                p.retire_401k, p.health_care
            FROM payroll p
            JOIN employees e ON e.empid = p.empid
            WHERE p.empid = ?
            ORDER BY p.pay_date DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, empid);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                statements.add(new PayStatement(
                    rs.getInt("empid"),
                    rs.getString("name"),
                    rs.getString("pay_date"),
                    rs.getDouble("earnings"),
                    rs.getDouble("fed_tax"),
                    rs.getDouble("fed_med"),
                    rs.getDouble("fed_SS"),
                    rs.getDouble("state_tax"),
                    rs.getDouble("retire_401k"),
                    rs.getDouble("health_care")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return statements;
    }
    
    // gets pay history for all employees (admin view)
    public static List<PayStatement> getAllPayStatements() {
        List<PayStatement> list = new ArrayList<>();
    
        String query = """
            SELECT 
                e.empid, 
                CONCAT(e.Fname, ' ', e.Lname) AS name,
                p.pay_date, p.earnings, p.fed_tax, p.fed_med, p.fed_SS, 
                p.state_tax, p.retire_401k, p.health_care
            FROM payroll p
            JOIN employees e ON e.empid = p.empid
            ORDER BY e.empid, p.pay_date DESC
        """;
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                list.add(new PayStatement(
                    rs.getInt("empid"),
                    rs.getString("name"),
                    rs.getString("pay_date"),
                    rs.getDouble("earnings"),
                    rs.getDouble("fed_tax"),
                    rs.getDouble("fed_med"),
                    rs.getDouble("fed_SS"),
                    rs.getDouble("state_tax"),
                    rs.getDouble("retire_401k"),
                    rs.getDouble("health_care")
                ));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return list;
    }
    
    public static void updateSalaries(double min, double max, double percent) {
        // Task 6: Update salary range
    }
}