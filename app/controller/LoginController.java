package app.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import app.dao.AuthDAO;
import app.dao.EmployeeDAO;
import app.model.User;
import app.view.DashboardView;
import app.view.LoginView;

public class LoginController {

    public static void loginMenu() {
        // launches the login GUI
        LoginView.display();
    }

    public static void handleLogin(String email, char[] password, JFrame frame) {
        User user = AuthDAO.login(email);
        if (user != null) {
            user.setEmployee(EmployeeDAO.getEmployeeById(user.getEmpid())); 
            JOptionPane.showMessageDialog(frame, "Welcome, " + user.getFname() + "!");
            frame.dispose();
            DashboardView.display(user);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}


