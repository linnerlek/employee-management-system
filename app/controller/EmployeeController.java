package app.controller;

import app.model.User;

public class EmployeeController {

    public static void employeeMenu(User user) {
        // Launch the Employee dashboard GUI
        app.view.Dashboard.display(user);
    }

    public static void viewOwnData() {
        // Task 3: Employee views their own data
    }

    public static void viewOwnPayStatements() {
        // Task 5 (Employee): View own pay history
    }
}
