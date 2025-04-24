package app.dao;

import app.db.DBConnection;
import app.model.MonthlyPayRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // Fetches total pay for each division by year and month
    public static List<MonthlyPayRecord> getMonthlyPayByDivision() {
        String sql = """
                SELECT
                    d.Name as division, SUM(p.earnings) AS total_earnings,
                    DATE_FORMAT(p.pay_date, '%Y-%m') as formatted_date
                FROM division d
                INNER JOIN employee_division ed
                ON d.ID = ed.div_ID
                INNER JOIN payroll p
                ON ed.empid = p.empid
                GROUP BY d.Name, DATE_FORMAT(p.pay_date, '%Y-%m');
                """;
        
        List<MonthlyPayRecord> records = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) { 
                String[] dateParts = rs.getString("formatted_date").split("-");
                String year = dateParts[0];
                String month = dateParts[1];

                records.add(new MonthlyPayRecord(null, rs.getDouble("total_earnings"), year, month, rs.getString("division")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    // Fetches total pay for each job title by year and month
    public static List<MonthlyPayRecord> getMonthlyPayByJobTitle() {
        String sql = """
                SELECT
                    jt.job_title AS title, SUM(p.earnings) AS total_earnings, 
                    DATE_FORMAT(p.pay_date, '%Y-%m') AS formatted_date
                FROM job_titles jt
                INNER JOIN employee_job_titles ejt
                ON jt.job_title_id = ejt.job_title_id
                INNER JOIN payroll p
                ON ejt.empid = p.empid
                GROUP BY title, DATE_FORMAT(p.pay_date, '%Y-%m');
                """;

        List<MonthlyPayRecord> records = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] dateParts = rs.getString("formatted_date").split("-");
                String year = dateParts[0];
                String month = dateParts[1];
                
                records.add(new MonthlyPayRecord(rs.getString("title"), rs.getDouble("total_earnings"), year, month, null));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void getFullEmployeeReport() {
        // Task 10
    }
}
