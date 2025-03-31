package app.dao;

import app.db.DBConnection;
import java.sql.*;

public class ReportDAO {
    public static int getTotalEmployees() {
        String sql = "SELECT COUNT(*) FROM employees";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getTotalDivisions() {
        String sql = "SELECT COUNT(*) FROM division";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static void getMonthlyPayByDivision() {
        // Task 8
    }

    public static void getMonthlyPayByJobTitle() {
        // Task 9
    }

    public static void getFullEmployeeReport() {
        // Task 10
    }
}
