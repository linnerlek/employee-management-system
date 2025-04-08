package app.dao;

import app.db.DBConnection;
import app.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static List<Employee> getEmployeesByName(String name) {
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
            WHERE e.Fname LIKE ? 
            OR e.Lname LIKE ?
                """;
                
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
   
            stmt.setString(1, "%" + name + "%");
            stmt.setString(2, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
        
            List<Employee> employees = new ArrayList<>();    
            while (rs.next()) {
                int empid = rs.getInt("empid");
                String lastPaidDate = PayrollDAO.getLastPayDate(empid);
                employees.add(new Employee(
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
            ));
        } 
        if (employees.isEmpty()) {
            System.err.println("Employee " + name + " not found.");
            return Collections.emptyList();
        }
        else {
            return employees;
        }
        
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Employee getEmployeeBySsn(String ssn) {
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
            WHERE e.SSN = ?
                """;
                
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                
            stmt.setString(1, ssn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int empid = rs.getInt("empid");
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
            } 
            else {
                System.err.println("No employee found with Social Security Number: " + ssn);
                return null;
            }       
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Employee> getEmployeesByDob(String dob){
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
            WHERE a.dob = ?
                """;
                try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                stmt.setString(1, dob);
                ResultSet rs = stmt.executeQuery();
    
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    int empid = rs.getInt("empid");
                    String lastPaidDate = PayrollDAO.getLastPayDate(empid);
                    employees.add(new Employee(
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
                    ));
                } 
                if (employees.isEmpty()) {
                    System.err.println("No employee found with date of birth: " + dob);
                    return Collections.emptyList();
                }
                else {
                    return employees;
                }       
            }
            catch (SQLException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        
    }
}