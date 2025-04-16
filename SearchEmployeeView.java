package app.view;

import app.controller.AdminController;
import app.model.Employee;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Title at the top
        JLabel titleLabel = new JLabel("Search Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        if (matches.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No employees found.", SwingConstants.CENTER);
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            panel.add(noResultsLabel, BorderLayout.CENTER);
            return panel;
        }
        // Create scroll panel for results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        for (Employee emp : matches) {
            String name = emp.getFname() + " " + emp.getLname();
            
            // Create result card with employee info
            JPanel employeeCard = new JPanel(new BorderLayout());
            employeeCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            // Main info section
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

            // Include additional info if available
            String additionalInfo = "ID: " + emp.getEmpid() + " | " + emp.getJobTitle();
            if (!emp.getDivisionName().equals("Missing")) {
                additionalInfo += " | " + emp.getDivisionName();
            }
            JLabel detailsLabel = new JLabel(additionalInfo);
            detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createHorizontalStrut(15));
            infoPanel.add(detailsLabel);

            employeeCard.add(infoPanel, BorderLayout.CENTER);

            // Action buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton viewButton = new JButton("View");
            viewButton.setFocusPainted(false);
            viewButton.addActionListener(e -> AdminController.showResult(emp, name));

            JButton updateButton = new JButton("Update");
            updateButton.setFocusPainted(false);
            updateButton.addActionListener(e -> {
                // Create and display update dialog
                JDialog updateDialog = UpdateEmployeeView.createDialog(emp);
                updateDialog.setVisible(true);
            });

            buttonPanel.add(viewButton);
            buttonPanel.add(updateButton);

            employeeCard.add(buttonPanel, BorderLayout.EAST);

            // Add the employee card to results
            resultsPanel.add(employeeCard);
            resultsPanel.add(Box.createVerticalStrut(5));
        }
        // Add scroll capability
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void displayEmployeeResults(List<Employee> matches) {
        JPanel resultsPanel = createEmployeeResultsPanel(matches);

        JDialog dialog = new JDialog();
        dialog.setTitle("Employee Search Results");
        dialog.setModal(true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        dialog.add(resultsPanel, BorderLayout.CENTER);

        // Add close button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

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
