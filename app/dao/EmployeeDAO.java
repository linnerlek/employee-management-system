package app.dao;

import app.model.Employee;
import app.db.DBConnection;
import java.sql.*;

public class EmployeeDAO {

    public static void getEmployeeById(int empid) {
        // Used in viewOwnData() and search
    }

    public static void searchByFields(String name, String ssn, String dob) {
        // Task 2: Search logic
    }

    public static void updateEmployee(int empid, String phone, String address) {
        // Task 4: Update logic
    }

    public static void insertEmployee(Employee emp) {
        // Task 7: Multi-table insert
    }
}
