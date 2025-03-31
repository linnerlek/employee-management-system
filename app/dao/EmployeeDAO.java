package app.dao;

import app.model.Employee;
import app.db.DBConnection;

import java.sql.*;

public class EmployeeDAO {
    // loads full employee data with joined tables
    public static Employee getEmployeeById(int empid) {
        String query = """
            SELECT 
                e.empid, e.Fname, e.Lname, e.email, e.SSN, 
                e.HireDate, e.Salary, jt.job_title,
                a.street, a.zip, a.gender, a.race, a.dob, a.phone,
                c.city_name, s.state_name,
                d.Name AS division_name
            FROM employees e
            LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid
            LEFT JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id
            LEFT JOIN address a ON e.empid = a.empid
            LEFT JOIN city c ON a.city_id = c.city_id
            LEFT JOIN state s ON a.state_id = s.state_id
            LEFT JOIN employee_division ed ON e.empid = ed.empid
            LEFT JOIN division d ON ed.div_ID = d.ID
            WHERE e.empid = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, empid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String lastPaidDate = PayrollDAO.getLastPayDate(empid);
                return new Employee(
                    empid,
                    rs.getString("Fname"),
                    rs.getString("Lname"),
                    rs.getString("email"),
                    rs.getString("SSN"),
                    rs.getString("HireDate"),
                    rs.getString("Salary"),
                    rs.getString("job_title"),
                    rs.getString("street"),
                    rs.getString("zip"),
                    rs.getString("gender"),
                    rs.getString("race"),
                    rs.getString("dob"),
                    rs.getString("phone"),
                    rs.getString("city_name"),
                    rs.getString("state_name"),
                    rs.getString("division_name"),
                    lastPaidDate
                );
            } else {
                System.err.println("No employee found for empid: " + empid);
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}