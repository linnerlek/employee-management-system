package app.controller;

import app.dao.EmployeeDAO;
import app.dao.PayrollDAO;
import app.model.Employee;
import app.model.User;
import app.view.SearchEmployeeView;
import java.util.List;
import javax.swing.*;

import app.view.SearchEmployeeView;
import app.model.MonthlyPayRecord;

import javax.swing.*;
import java.sql.*;
import app.dao.EmployeeDAO;
import app.dao.ReportDAO;
import app.model.Employee;
import app.model.MonthlyPayDTO;

import java.util.*;

public class AdminController {

    public static void adminMenu(User user) {
        // Launch the Admin dashboard GUI
        app.view.DashboardView.display(user);
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

    public static int updateSalaryRange(double min, double max, double percent) {
    return PayrollDAO.updateSalaries(min, max, percent);
}

    public static void insertNewEmployee() {
        // Task 7: Insert into multiple related tables (employees, address, etc.)
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
        // Task 10: Full employee report
    }
}