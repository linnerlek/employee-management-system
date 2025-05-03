import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EmployeeDA0 {
    public boolean insertEmployee(employeeData data) throws Exception {
        // Adjust 
        String url = "jdbc:mysql://localhost:3306/employeeData";
        String user = "root";
        String password = "password";

        String sql = "INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, data.getEmpid());
            stmt.setString(2, data.getFname());
            stmt.setString(3, data.getLname());
            stmt.setString(4, data.getEmail());
            stmt.setDate(5, java.sql.Date.valueOf(data.getHireDate()));
            stmt.setBigDecimal(6, data.getSalary());
            stmt.setString(7, data.getSsn());

            return stmt.executeUpdate() > 0;
        }
    }
}
