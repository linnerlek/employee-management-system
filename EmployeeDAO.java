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

    public static boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees e SET e.Fname = ?, e.Lname = ?, e.email = ? WHERE e.empid = ?";
        
        String addressQuery = """
            UPDATE address a
            SET a.street = ?, a.zip = ?, a.gender = ?, 
                a.race = ?, a.dob = ?, a.phone = ?
            WHERE a.empid = ?
            """;
    
        // Modified query to handle city/state more robustly
        String cityQuery = "SELECT COUNT(*) FROM city WHERE city_name = ?";
        String stateQuery = "SELECT COUNT(*) FROM state WHERE state_name = ?";
        
        String cityStateQuery = """
            UPDATE address a
            SET a.city_id = (SELECT city_id FROM city WHERE city_name = ?),
                a.state_id = (SELECT state_id FROM state WHERE state_name = ?)
            WHERE a.empid = ?
            """;
    
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement addrStmt = null;
        PreparedStatement cityCheckStmt = null;
        PreparedStatement stateCheckStmt = null;
        PreparedStatement cityStateStmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Basic employee update
            stmt = conn.prepareStatement(query);
            stmt.setString(1, employee.getFname().equals("Missing") ? null : employee.getFname());
            stmt.setString(2, employee.getLname().equals("Missing") ? null : employee.getLname());
            stmt.setString(3, employee.getEmail().equals("Missing") ? null : employee.getEmail());
            stmt.setInt(4, employee.getEmpid());
            int empRowsUpdated = stmt.executeUpdate();
            System.out.println("Employee rows updated: " + empRowsUpdated);
            
            // Address update
            addrStmt = conn.prepareStatement(addressQuery);
            addrStmt.setString(1, employee.getStreet().equals("Missing") ? null : employee.getStreet());
            addrStmt.setString(2, employee.getZip().equals("Missing") ? null : employee.getZip());
            addrStmt.setString(3, employee.getGender().equals("Missing") ? null : employee.getGender());
            addrStmt.setString(4, employee.getRace().equals("Missing") ? null : employee.getRace());
            addrStmt.setString(5, employee.getDob().equals("Missing") ? null : employee.getDob());
            addrStmt.setString(6, employee.getPhone().equals("Missing") ? null : employee.getPhone());
            addrStmt.setInt(7, employee.getEmpid());
            int addrRowsUpdated = addrStmt.executeUpdate();
            System.out.println("Address rows updated: " + addrRowsUpdated);
            
            // City/State update if not missing
            if (!employee.getCityName().equals("Missing") && !employee.getStateName().equals("Missing")) {
                // Check if city exists
                cityCheckStmt = conn.prepareStatement(cityQuery);
                cityCheckStmt.setString(1, employee.getCityName());
                ResultSet cityRs = cityCheckStmt.executeQuery();
                cityRs.next();
                int cityExists = cityRs.getInt(1);
                
                // Check if state exists
                stateCheckStmt = conn.prepareStatement(stateQuery);
                stateCheckStmt.setString(1, employee.getStateName());
                ResultSet stateRs = stateCheckStmt.executeQuery();
                stateRs.next();
                int stateExists = stateRs.getInt(1);
                
                if (cityExists > 0 && stateExists > 0) {
                    // Both city and state exist, proceed with update
                    cityStateStmt = conn.prepareStatement(cityStateQuery);
                    cityStateStmt.setString(1, employee.getCityName());
                    cityStateStmt.setString(2, employee.getStateName());
                    cityStateStmt.setInt(3, employee.getEmpid());
                    
                    int cityStateRowsUpdated = cityStateStmt.executeUpdate();
                    System.out.println("City/State rows updated: " + cityStateRowsUpdated);
                    
                    if (cityStateRowsUpdated == 0) {
                        // Failed to update city/state but we'll continue with other updates
                        System.out.println("Warning: City/State update failed but continuing transaction");
                    }
                } else {
                    System.out.println("Warning: City (" + cityExists + ") or State (" + stateExists + ") not found in database");
                }
            }
            
            // Commit transaction
            conn.commit();
            System.out.println("Transaction committed successfully");
            success = true;
            
        } catch (SQLException e) {
            // Error handling
            System.err.println("SQL Error during update: " + e.getMessage());
            
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error");
                } catch (SQLException ex) {
                    System.err.println("Failed to rollback transaction: " + ex.getMessage());
                }
            }
        } finally {
            // Resource cleanup
            try {
                if (stmt != null) stmt.close();
                if (addrStmt != null) addrStmt.close();
                if (cityCheckStmt != null) cityCheckStmt.close();
                if (stateCheckStmt != null) stateCheckStmt.close();
                if (cityStateStmt != null) cityStateStmt.close();
                
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        
        return success;
    }

}