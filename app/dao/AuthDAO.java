package app.dao;

import app.model.User;
import app.db.DBConnection;

import java.sql.*;

public class AuthDAO {
    // authenticates user by email, returns user obj if found
    public static User login(String email) {
        String query = "SELECT e.empid, e.email, e.fname, ej.job_title_id " +
               "FROM employees e " +
               "JOIN employee_job_titles ej ON e.empid = ej.empid " +
               "WHERE e.email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int empid = rs.getInt("empid");
                String jobTitleId = rs.getString("job_title_id");
                String fname = rs.getString("fname");
                return new User(empid, email, "mockPassword", fname, jobTitleId);
            }
            else {
                return null;  // email not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}