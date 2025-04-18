package app.controller;

import app.view.SearchEmployeeView;

import javax.swing.*;
import app.dao.EmployeeDAO;
import app.model.Employee;

import java.util.List;

public class AdminController {

    public static void adminMenu() {
        // Launch the Admin dashboard GUI
        app.view.Dashboard.display();
    }

    public static void searchEmployee(String searchType) {
        // Task 2: Admin searches employee by name, empid, SSN, DOB
        switch (searchType.toUpperCase()) {
            case "ID" -> searchByEmployeeID();
            case "NAME" -> searchByName();
            case "SSN" -> searchBySsn();
            case "DOB" -> searchByDob();
        }
    }

    private static void searchByEmployeeID() {
        String input = promptUser("Enter Employee ID");
        if (input == null) {
            return;
        }

        try {
            int empId = Integer.parseInt(input);
            Employee emp = EmployeeDAO.getEmployeeById(empId);
            showResult(emp, "ID: " + empId);
        } 
        catch (NumberFormatException e) {
            showError("Invalid ID format. Please enter a number.");
        }
    }

    private static void searchByName() {
        String input = promptUser("Enter Employee Name");
        if (input == null || input.isEmpty()) {
            return;
        }

        List<Employee> matches = EmployeeDAO.getEmployeesByName(input);
        SearchEmployeeView.createEmployeeResultsPanel(matches);

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee " + input + " not found.", "No results.", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            SearchEmployeeView.displayEmployeeResults(matches);
        }
    }

    private static void searchBySsn() {
        String input = promptUser("Enter a Social Security Number (xxx-xx-xxxx)");
        if (input == null) {
            return;
        }

        try {
            if (!input.matches("\\d{3}-\\d{2}-\\d{4}")) {
                throw new IllegalArgumentException("Invalid SSN format.");
            }

            Employee emp = EmployeeDAO.getEmployeeBySsn(input.trim());
            showResult(emp, "SSN: " + input);
        }
        catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private static void searchByDob() {
        String input = promptUser("Enter a Date of Birth (YYYY-MM-DD)");
        if (input == null || input.isEmpty()) {
            return;
        }

        try {
            if (!input.matches("\\d{4}-\\d{2}-\\d{2}")){
                throw new IllegalArgumentException("Invalid DOB format.");
            }
        } 
        catch (IllegalArgumentException e) {
            showError(e.getMessage());
            return;
        }

        List<Employee> matches = EmployeeDAO.getEmployeesByDob(input);
        SearchEmployeeView.createEmployeeResultsPanel(matches);

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee with date of birth " + input + " not found.",
            "No results.", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            SearchEmployeeView.displayEmployeeResults(matches);
        }
    }

    private static String promptUser(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        else {
            return input.trim();
        }
    }

    public static void showResult(Employee emp, String searchCriteria) {
        if (emp == null) {
            showError("No employee found for " + searchCriteria);
            return;
        }

        String result = String.format("Employee Details: %n ID: %d %n Name: %s %s %n Email: %s %n SSN: %s %n Hire Date: %s %n Salary: %s %n Job Title: %s %n Division: %s %n Address: %s %s, %s %s %n Demographics: %s, %s %n DOB: %s %n Phone: %s %n Last Paid Date: %s", 
                emp.getEmpid(), emp.getFname(), emp.getLname(), 
                emp.getEmail(), emp.getSsn(), emp.getHireDate(), emp.getSalary(), emp.getJobTitle(), 
                emp.getDivisionName(), emp.getStreet(), emp.getCityName(), emp.getStateName(), emp.getZip(), 
                emp.getGender(), emp.getRace(), emp.getDob(), emp.getPhone(), emp.getLastPaidDate());

        JOptionPane.showMessageDialog(null, result, "Employee Found", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void updateEmployeeData() {
        // Task 4: Admin updates employee info
    }

    public static void viewAllPayStatements() {
        // Task 5: View all employees' pay history (admin view)
    }

    public static void updateSalaryRange(double min, double max, double percent) {
        // Task 6: Raise salary in specified range
    }

    public static void insertNewEmployee() {
        // Task 7: Insert into multiple related tables (employees, address, etc.)
    }

    public static void generatePayByDivision() {
        // Task 8: Monthly pay by division report
    }

    public static void generatePayByJobTitle() {
        // Task 9: Monthly pay by job title report
    }

    public static void generateFullReport() {
        // Task 10: Full employee report
    }
}
