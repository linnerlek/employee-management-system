package app.view;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.model.User;

public class EmployeeDashboard {
    public static void display(User user) {
    JFrame frame = new JFrame("Employee Dashboard");
    frame.setSize(800, 550);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel();
    panel.add(new JLabel("Welcome, " + user.getEmail()));
    panel.add(new JLabel("TODO: Add View Info and View Pay buttons"));
    frame.add(panel);
    frame.setVisible(true);
}

}
