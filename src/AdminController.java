import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AdminController {
    public String insertEmployee(employeeData data) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeeData", "root", "password")) {
            conn.setAutoCommit(false);

            // 1. Insert into employees
            String insertEmp = "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertEmp)) {
                ps.setInt(1, data.getEmpid());
                ps.setString(2, data.getFname());
                ps.setString(3, data.getLname());
                ps.setString(4, data.getEmail());
                ps.setDate(5, java.sql.Date.valueOf(data.getHireDate()));
                ps.setBigDecimal(6, data.getSalary());
                ps.setString(7, data.getSsn());
                ps.executeUpdate();
            }

            // 2. Insert into employee_job_titles
            String insertJob = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertJob)) {
                ps.setInt(1, data.getEmpid());
                ps.setInt(2, data.getJobTitleId());
                ps.executeUpdate();
            }

            // 3. Insert into employee_division
            String insertDiv = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertDiv)) {
                ps.setInt(1, data.getEmpid());
                ps.setInt(2, data.getDivisionId());
                ps.executeUpdate();
            }

            conn.commit();
            return "Employee inserted successfully.";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Insertion failed: " + ex.getMessage();
        }
    }
}
