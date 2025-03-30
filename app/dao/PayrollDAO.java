package app.dao;

import app.db.DBConnection;
import java.sql.*;

public class PayrollDAO {

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
    

    public static void getPayStatements(int empid) {
        // Task 5: Pay history
    }

    public static void updateSalaries(double min, double max, double percent) {
        // Task 6: Update salary range
    }
}
