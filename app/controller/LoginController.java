package app.controller;

import app.view.LoginView;

public class LoginController {
    public static void loginMenu() {
        // Launch the Login GUI
        LoginView.display();
        // TODO: collect email and password input

        // TODO: call AuthDAO.login(email, password) to fetch user
        // If login successful, check if user isAdmin()
        // Route to AdminController.adminMenu() or EmployeeController.employeeMenu(user)
    }
}
