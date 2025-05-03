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
    
    /**
     * Updates employee information with validation and error reporting
     * Returns UpdateResult object with success/failure status and error message
     */
    public static UpdateResult updateEmployee(Employee employee) {
        // Main employee update query
        String employeeQuery = "UPDATE employees SET Fname = ?, Lname = ?, email = ? WHERE empid = ?";
        
        // Check if address exists for this employee
        String checkAddressQuery = "SELECT COUNT(*) FROM address WHERE empid = ?";
        
        // Address update query that creates city and state if they don't already exist
        String addressUpdateQuery = "UPDATE address SET street = ?, zip = ?, gender = ?, race = ?, dob = ?, phone = ?, " +
                                   "city_id = ?, state_id = ? WHERE empid = ?";

        // Insert address query for cases where address doesn't already exist
        String addressInsertQuery = "INSERT INTO address (empid, street, zip, gender, race, dob, phone, city_id, state_id) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        UpdateResult result = new UpdateResult(false, "Unknown error occurred");

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Handle empty or "Missing" values for city/state
            String cityName = (employee.getCityName() == null || employee.getCityName().isEmpty() || 
                            employee.getCityName().equals("Missing")) ? null : employee.getCityName();
            String stateName = (employee.getStateName() == null || employee.getStateName().isEmpty() || 
                            employee.getStateName().equals("Missing")) ? null : employee.getStateName();
            
            // Variables to hold IDs for city and state
            Integer cityId = null;
            Integer stateId = null;
            
            // Get or create city ID if cityName is provided
            if (cityName != null) {
                cityId = getOrCreateCity(cityName, conn);
                if (cityId == null) {
                    throw new SQLException("Failed to get or create city: " + cityName);
                }
            }
            
            // Get or create state ID if stateName is provided
            if (stateName != null) {
                stateId = getOrCreateState(stateName, conn);
                if (stateId == null) {
                    throw new SQLException("Failed to get or create state: " + stateName);
                }
            }
            
            // Update basic employee information
            try (PreparedStatement stmt = conn.prepareStatement(employeeQuery)) {
                stmt.setString(1, employee.getFname());
                stmt.setString(2, employee.getLname());
                stmt.setString(3, employee.getEmail());
                stmt.setInt(4, employee.getEmpid());
                
                int empRowsUpdated = stmt.executeUpdate();
                
                if (empRowsUpdated == 0) {
                    throw new SQLException("Employee update failed, no rows affected.");
                }
            }
            
            // Check if address exists for this employee
            boolean addressExists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAddressQuery)) {
                checkStmt.setInt(1, employee.getEmpid());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    addressExists = true;
                }
            }
            
            int addrRowsAffected = 0;
            
            // Handle NULL or "Missing" values for address fields
            String street = (employee.getStreet() == null || employee.getStreet().isEmpty() || 
                        employee.getStreet().equals("Missing")) ? null : employee.getStreet();
            String zip = (employee.getZip() == null || employee.getZip().isEmpty() || 
                        employee.getZip().equals("Missing")) ? null : employee.getZip();
            String gender = (employee.getGender() == null || employee.getGender().isEmpty() || 
                        employee.getGender().equals("Missing")) ? null : employee.getGender();
            String race = (employee.getRace() == null || employee.getRace().isEmpty() || 
                        employee.getRace().equals("Missing")) ? null : employee.getRace();
            String dob = (employee.getDob() == null || employee.getDob().isEmpty() || 
                        employee.getDob().equals("Missing")) ? null : employee.getDob();
            String phone = (employee.getPhone() == null || employee.getPhone().isEmpty() || 
                        employee.getPhone().equals("Missing")) ? null : employee.getPhone();
            
            if (addressExists) {
                // Update existing address
                try (PreparedStatement addrStmt = conn.prepareStatement(addressUpdateQuery)) {
                    // Set parameters, handling NULL values
                    if (street == null) addrStmt.setNull(1, java.sql.Types.VARCHAR);
                    else addrStmt.setString(1, street);
                    
                    if (zip == null) addrStmt.setNull(2, java.sql.Types.VARCHAR);
                    else addrStmt.setString(2, zip);
                    
                    if (gender == null) addrStmt.setNull(3, java.sql.Types.VARCHAR);
                    else addrStmt.setString(3, gender);
                    
                    if (race == null) addrStmt.setNull(4, java.sql.Types.VARCHAR);
                    else addrStmt.setString(4, race);
                    
                    if (dob == null) addrStmt.setNull(5, java.sql.Types.VARCHAR);
                    else addrStmt.setString(5, dob);
                    
                    if (phone == null) addrStmt.setNull(6, java.sql.Types.VARCHAR);
                    else addrStmt.setString(6, phone);
                    
                    if (cityId == null) addrStmt.setNull(7, java.sql.Types.INTEGER);
                    else addrStmt.setInt(7, cityId);
                    
                    if (stateId == null) addrStmt.setNull(8, java.sql.Types.INTEGER);
                    else addrStmt.setInt(8, stateId);
                    
                    addrStmt.setInt(9, employee.getEmpid());
                    
                    addrRowsAffected = addrStmt.executeUpdate();
                }
            } else {
                // Insert new address - only if at least one address field is provided
                if (street != null || zip != null || gender != null || race != null || 
                    dob != null || phone != null || cityId != null || stateId != null) {
                    
                    try (PreparedStatement addrStmt = conn.prepareStatement(addressInsertQuery)) {
                        addrStmt.setInt(1, employee.getEmpid());
                        
                        if (street == null) addrStmt.setNull(2, java.sql.Types.VARCHAR);
                        else addrStmt.setString(2, street);
                        
                        if (zip == null) addrStmt.setNull(3, java.sql.Types.VARCHAR);
                        else addrStmt.setString(3, zip);
                        
                        if (gender == null) addrStmt.setNull(4, java.sql.Types.VARCHAR);
                        else addrStmt.setString(4, gender);
                        
                        if (race == null) addrStmt.setNull(5, java.sql.Types.VARCHAR);
                        else addrStmt.setString(5, race);
                        
                        if (dob == null) addrStmt.setNull(6, java.sql.Types.VARCHAR);
                        else addrStmt.setString(6, dob);
                        
                        if (phone == null) addrStmt.setNull(7, java.sql.Types.VARCHAR);
                        else addrStmt.setString(7, phone);
                        
                        if (cityId == null) addrStmt.setNull(8, java.sql.Types.INTEGER);
                        else addrStmt.setInt(8, cityId);
                        
                        if (stateId == null) addrStmt.setNull(9, java.sql.Types.INTEGER);
                        else addrStmt.setInt(9, stateId);
                        
                        addrRowsAffected = addrStmt.executeUpdate();
                    }
                } else {
                    // No address fields provided, so skip address insert
                    addrRowsAffected = 1; // Pretend it worked
                }
            }
            
            if (addrRowsAffected == 0 && (street != null || zip != null || gender != null || race != null || 
                                        dob != null || phone != null || cityName != null || stateName != null)) {
                throw new SQLException("Address operation failed, no rows affected.");
            }

            // Update job title and division 
            try {
                updateJobTitleAndDivision(employee, conn);
            } catch (SQLException e) {
                throw new SQLException("Failed to update job title or division: " + e.getMessage());
            }
            
            // Commit the transaction
            conn.commit();
            result.setSuccess(true);
            result.setMessage("Employee updated successfully");
            
        } catch (SQLException e) {
            String errorMessage = "SQL Error: " + e.getMessage();
            result.setMessage(errorMessage);
            
            // Rollback on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Just continue with error handling
                }
            }
        } finally {
            // Close connection and restore auto-commit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Just continue with error handling
                }
            }
        }
        
        return result;
    }

    /**
     * Get or create a city and return its ID
     * cityName: the name of the city
     * conn: the database connection
     * Return the city ID or null if an error occurred
     */
    private static Integer getOrCreateCity(String cityName, Connection conn) throws SQLException {
        if (cityName == null || cityName.trim().isEmpty()) {
            return null;
        }
        
        // First check if the city exists
        String checkQuery = "SELECT city_id FROM city WHERE city_name = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("city_id");
            } else {
                // Get the next available city_id
                String getMaxIdQuery = "SELECT COALESCE(MAX(city_id), 0) + 1 FROM city";
                int nextCityId;
                
                try (PreparedStatement maxStmt = conn.prepareStatement(getMaxIdQuery)) {
                    ResultSet maxRs = maxStmt.executeQuery();
                    if (maxRs.next()) {
                        nextCityId = maxRs.getInt(1);
                    } else {
                        nextCityId = 1; // Default if no cities exist
                    }
                }
                
                // Insert the new city
                String insertQuery = "INSERT INTO city (city_id, city_name) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, nextCityId);
                    insertStmt.setString(2, cityName);
                    int rowsAffected = insertStmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        return nextCityId;
                    } else {
                        return null;
                    }
                }
            }
        }
    }
    
    /**
     * Get or create a state and return its ID
     * stateName The name of the state
     * conn The database connection
     * The state ID or null if an error occurred
     */
    private static Integer getOrCreateState(String stateName, Connection conn) throws SQLException {
        if (stateName == null || stateName.trim().isEmpty()) {
            return null;
        }
        
        // First check if the state exists
        String checkQuery = "SELECT state_id FROM state WHERE state_name = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            stmt.setString(1, stateName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("state_id");
            } else {
                // Get the next available state_id
                String getMaxIdQuery = "SELECT COALESCE(MAX(state_id), 0) + 1 FROM state";
                int nextStateId;
                
                try (PreparedStatement maxStmt = conn.prepareStatement(getMaxIdQuery)) {
                    ResultSet maxRs = maxStmt.executeQuery();
                    if (maxRs.next()) {
                        nextStateId = maxRs.getInt(1);
                    } else {
                        nextStateId = 1; // Default if no states exist
                    }
                }
                
                // Insert the new state
                String insertQuery = "INSERT INTO state (state_id, state_name) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, nextStateId);
                    insertStmt.setString(2, stateName);
                    int rowsAffected = insertStmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        return nextStateId;
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private static void updateJobTitleAndDivision(Employee employee, Connection conn) throws SQLException {
        // Update job title
        if (employee.getJobTitle() != null && !employee.getJobTitle().isEmpty()) {
            // First get the job_title_id
            int jobTitleId;
            String jobTitleQuery = "SELECT job_title_id FROM job_titles WHERE job_title = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(jobTitleQuery)) {
                stmt.setString(1, employee.getJobTitle());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    jobTitleId = rs.getInt("job_title_id");
                    
                    // Now update employee_job_titles
                    String updateQuery = "UPDATE employee_job_titles SET job_title_id = ? WHERE empid = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, jobTitleId);
                        updateStmt.setInt(2, employee.getEmpid());
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Job title doesn't exist, create it first
                    String insertJobTitleQuery = "INSERT INTO job_titles (job_title) VALUES (?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertJobTitleQuery, 
                                                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setString(1, employee.getJobTitle());
                        insertStmt.executeUpdate();
                        
                        // Get the new job_title_id
                        ResultSet keys = insertStmt.getGeneratedKeys();
                        if (keys.next()) {
                            jobTitleId = keys.getInt(1);
                            
                            // Now update employee_job_titles
                            String updateQuery = "UPDATE employee_job_titles SET job_title_id = ? WHERE empid = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, jobTitleId);
                                updateStmt.setInt(2, employee.getEmpid());
                                updateStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
        
        // Update division name
        if (employee.getDivisionName() != null && !employee.getDivisionName().isEmpty()) {
            // First get the division ID
            int divisionId;
            String divisionQuery = "SELECT ID FROM division WHERE Name = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(divisionQuery)) {
                stmt.setString(1, employee.getDivisionName());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    divisionId = rs.getInt("ID");
                    
                    // Now update employee_division
                    String updateQuery = "UPDATE employee_division SET div_ID = ? WHERE empid = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, divisionId);
                        updateStmt.setInt(2, employee.getEmpid());
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Division doesn't exist, create it first
                    String insertDivisionQuery = "INSERT INTO division (Name) VALUES (?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertDivisionQuery, 
                                                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setString(1, employee.getDivisionName());
                        insertStmt.executeUpdate();
                        
                        // Get the new division ID
                        ResultSet keys = insertStmt.getGeneratedKeys();
                        if (keys.next()) {
                            divisionId = keys.getInt(1);
                            
                            // Now update employee_division
                            String updateQuery = "UPDATE employee_division SET div_ID = ? WHERE empid = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, divisionId);
                                updateStmt.setInt(2, employee.getEmpid());
                                updateStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Class to hold update result information
    public static class UpdateResult {
        private boolean success;
        private String message;
        
        public UpdateResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}