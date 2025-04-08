package app.view;

import app.controller.AdminController;
import app.model.Employee;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SearchEmployeeView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Search Employee", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Create a separate panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(200));
        
        // Buttons for search filters
        buttonPanel.add(createSearchButton("Search by Employee ID", e -> AdminController.searchEmployee("ID")));
        buttonPanel.add(Box.createVerticalStrut(10));

        buttonPanel.add(createSearchButton("Search by Employee Name", e -> AdminController.searchEmployee("NAME")));
        buttonPanel.add(Box.createVerticalStrut(10));

        buttonPanel.add(createSearchButton("Search by Social Security Number", e -> AdminController.searchEmployee("SSN")));
        buttonPanel.add(Box.createVerticalStrut(10));

        buttonPanel.add(createSearchButton("Search by Date of Birth", e -> AdminController.searchEmployee("DOB")));

        // add button panel to main panel
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel createEmployeeResultsPanel(List<Employee> matches){
        JPanel panel = new JPanel(new FlowLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (matches.isEmpty()) {
            panel.add(new JLabel("No employees found."));
            return panel;
        }

        for (Employee emp : matches) {
            String name = emp.getFname() + " " + emp.getLname();
            JButton button = new JButton(name);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);

            button.addActionListener(e -> AdminController.showResult(emp, name));

            panel.add(button);
            panel.add(Box.createVerticalStrut(5));
        }

        return panel;
    }

    public static void displayEmployeeResults(List<Employee> matches) {
        JPanel resultsPanel = createEmployeeResultsPanel(matches);

        JDialog dialog = new JDialog();
        dialog.add(resultsPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static JButton createSearchButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }
}
