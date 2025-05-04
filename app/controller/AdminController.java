package app.controller;

import app.view.SearchEmployeeView;
import app.view.UpdateEmployeeView;


import javax.swing.*;
import app.dao.EmployeeDAO;
import app.dao.EmployeeDAO.UpdateResult;
import app.dao.PayrollDAO;
import app.model.Employee;
import app.model.MonthlyPayRecord;
import app.model.User;
import app.dao.ReportDAO;

import java.awt.Font;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Create panel for displaying results with an update button
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton updateButton = new JButton("Update Employee");
        // Add action listener to open update form
        updateButton.addActionListener(e -> {
            // Create and show update dialog
            JDialog updateDialog = UpdateEmployeeView.createDialog(emp);
            updateDialog.setVisible(true);
        });

        buttonPanel.add(updateButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        // Show the dialog with custom panel
        JOptionPane.showOptionDialog(null, panel, "Employee Found", 
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                    null, new Object[]{}, null);

    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean updateEmployeeData(Employee updatedEmployee) {
        // Task 4: Admin updates employee info
    
        // Validate that the employee exists
        Employee currentEmployee = EmployeeDAO.getEmployeeById(updatedEmployee.getEmpid());
        if (currentEmployee == null) {
            JOptionPane.showMessageDialog(null, 
                "Employee not found. Cannot update.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Call DAO to update employee data
        UpdateResult updateResult = EmployeeDAO.updateEmployee(updatedEmployee);
        
        if (updateResult.isSuccess()) {
            return true;
        } else {
            // Show specific error message from database operation
            JOptionPane.showMessageDialog(null, 
                "Update failed: " + updateResult.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void viewAllPayStatements() {
        // Task 5: View all employees' pay history (admin view)
    }

    public static int updateSalaryRange(double min, double max, double percent) {
        // Task 6: Raise salary in specified range
        return PayrollDAO.updateSalaries(min, max, percent);
    }

 // Task 7: Insert New Employee
    public static String insertNewEmployee(Employee data) {
        try (Connection conn = DriverManager.getConnection("jdbc:"url", "user", "password")) {
            conn.setAutoCommit(false);
    
            String insertEmp = "INSERT INTO employees (empid, fname, lname, email, hireDate, salary, ssn) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
    
            String insertJob = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertJob)) {
                ps.setInt(1, data.getEmpid());
                ps.setInt(2, data.getJobTitle());
                ps.executeUpdate();
            }
    
            String insertDiv = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertDiv)) {
                ps.setInt(1, data.getEmpid());
                ps.setInt(2, data.getDivisionName());
                ps.executeUpdate();
            }
    
            conn.commit();
            return "Employee inserted successfully.";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Insertion failed: " + ex.getMessage();
        }
    }

    public static Map<String, Map<String, List<MonthlyPayRecord>>> generatePayByDivision(List<MonthlyPayRecord> records) {
        Map<String, Map<String, List<MonthlyPayRecord>>> dateRecord = new HashMap<>();
        for (MonthlyPayRecord rec : records) {
            String year = rec.getYear();
            String month = rec.getMonth();

            if(!dateRecord.containsKey(year)) {
                dateRecord.put(year, new HashMap<>());
            }
            if(!dateRecord.get(year).containsKey(month)) {
                dateRecord.get(year).put(month, new ArrayList<>());
            }

            dateRecord.get(year).get(month).add(rec);
        }

        return dateRecord;
    }

    public static Map<String, Map<String, List<MonthlyPayRecord>>> generatePayByJobTitle(List<MonthlyPayRecord> records) {
        // Task 9: Monthly pay by job title report
        Map<String, Map<String, List<MonthlyPayRecord>>> dateRecord = new HashMap<>();
        for(MonthlyPayRecord rec : records) {
            String year = rec.getYear();
            String month = rec.getMonth();

            if(!dateRecord.containsKey(year)) {
                dateRecord.put(year, new HashMap<>());
            }
            if(!dateRecord.get(year).containsKey(month)) {
                dateRecord.get(year).put(month, new ArrayList<>());
            }

            dateRecord.get(year).get(month).add(rec);
        }

        return dateRecord;
    }

    // Generate a list of years
    public static List<String> getYearList(Map<String, Map<String, List<MonthlyPayRecord>>> records) {
        List<String> yearList = new ArrayList<>();
        
        for(String key : records.keySet()) {
            yearList.add(key);
        }

        return yearList;
    }

    public static List<String> getMonthList(Map<String, Map<String, List<MonthlyPayRecord>>> records, String selectedYear) {
        Map<String, List<MonthlyPayRecord>> yearData = records.get(selectedYear);
        List<String> monthList = new ArrayList(yearData.keySet());
        Collections.sort(monthList);
        return monthList;
    }

    // Method for output
    public static void printPayRecord(Map<String, Map<String, List<MonthlyPayRecord>>> records, String year, String month) {
        StringBuilder result = new StringBuilder();
        result.append("Report for ").append(month).append("/").append(year).append("\n\n");

        if(records.containsKey(year) && records.get(year).containsKey(month)) {
            List<MonthlyPayRecord> resultList = records.get(year).get(month);

            if(resultList.isEmpty()) {
                result.append("Record not found.");
            } else {
                for(MonthlyPayRecord rec : resultList) {
                    if(rec.getDivision() == null) {
                        result.append(String.format("%s | %.2f\n", rec.getJobTitle(), rec.getEarnings()));
                    }
                    if (rec.getJobTitle() == null) {
                        result.append(String.format("%s | %.2f\n", rec.getDivision(), rec.getEarnings()));
                    }
                    result.append(String.format("----------------------------------------------\n\n"));
                }
            }
        }
        
        JOptionPane.showMessageDialog(null, result.toString(), "Pay Report", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void generateFullReport() {
        ResultSet rs = ReportDAO.getFullEmployeeReport();
            if (rs == null) {
                JOptionPane.showMessageDialog(null, "Failed to retrieve report.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder report = new StringBuilder();
            report.append(String.format("%-5s %-15s %-15s %-10s %-20s %-15s%n", "ID", "First Name", "Last Name", "Salary", "Job Title", "Division"));
            report.append("-------------------------------------------------------------------------------\n");

            try {
                while (rs.next()) {
                    int id = rs.getInt("empid");
                    String fname = rs.getString("fname");
                    String lname = rs.getString("lname");
                    double salary = rs.getDouble("salary");
                    String jobTitle = rs.getString("job_title");
                    String division = rs.getString("division_name");

                    report.append(String.format("%-5d %-15s %-15s $%-9.2f %-20s %-15s%n", id, fname, lname, salary, jobTitle, division));
                }

                JTextArea textArea = new JTextArea(report.toString());
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(800, 400));

                JOptionPane.showMessageDialog(null, scrollPane, "Full Employee Report", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error displaying report.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
}
