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

    public static ResultSet getFullEmployeeReport() {
        String sql = """
                SELECT 
                    e.empid, 
                    e.fname, 
                    e.lname, 
                    e.salary, 
                    jt.job_title, 
                    d.Name AS division_name
                FROM employeedata.employees e
                LEFT JOIN employeedata.employee_job_titles ejt ON e.empid = ejt.empid
                LEFT JOIN employeedata.job_titles jt ON ejt.job_title_id = jt.job_title_id
                LEFT JOIN employeedata.employee_division ed ON e.empid = ed.empid
                LEFT JOIN employeedata.division d ON ed.div_ID = d.ID
                ORDER BY e.empid;
            """;

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
