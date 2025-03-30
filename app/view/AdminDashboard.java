package app.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminDashboard {
    public static void display() {
    JFrame frame = new JFrame("Admin Dashboard");
    frame.setSize(800, 550);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel();
    panel.add(new JLabel("Admin Dashboard - TODO: Add search, update, report buttons"));
    frame.add(panel);
    frame.setVisible(true);
}

}
