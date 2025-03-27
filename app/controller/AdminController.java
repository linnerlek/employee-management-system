package app.controller;

public class AdminController {

    public static void adminMenu() {
        // Launch the Admin dashboard GUI
        app.view.AdminDashboard.display();
    }

    public static void searchEmployee() {
        // Task 2: Admin searches employee by name, empid, SSN, DOB
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
