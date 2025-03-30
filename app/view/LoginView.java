package app.view;

import app.model.User;
import app.controller.AdminController;
import app.controller.EmployeeController;
import app.dao.AuthDAO;

import javax.swing.*;
import java.awt.*;

public class LoginView {
    public static void display() {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 90, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailText.getText();
            String password = new String(passwordText.getPassword());

            User user = AuthDAO.login(email, password);

            if (user != null) {
                JOptionPane.showMessageDialog(frame, "Welcome, " + user.getRole() + "!");
                frame.dispose();  // Close login window

                if (user.isAdmin()) {
                    AdminController.adminMenu();
                } else {
                    EmployeeController.employeeMenu(user);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
