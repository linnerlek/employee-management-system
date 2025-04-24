package app.view;

import app.dao.PayrollDAO;
import app.controller.AdminController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RaiseSalaryView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Raise Salary", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Center form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Creates the text for the minimum salary range
        JLabel minLabel = new JLabel("Min Salary:");

        JTextField minField = new JTextField();
                
        // Creates the text for the maximum salary range
        JLabel maxLabel = new JLabel("Max Salary:");

        JTextField maxField = new JTextField();

        // Creates the text for the percentage raise
        JLabel percentLabel = new JLabel("Raise %:");

        JTextField percentField = new JTextField();

        // Implements the button is pushed to cause the update in employee range
        JButton applyButton = new JButton("Apply Raise");

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);

        // Paneling for the Minimum
        formPanel.add(minLabel);
        formPanel.add(minField);
        
        // Paneling for the Maximum
        formPanel.add(maxLabel);
        formPanel.add(maxField);
        
        // Paneling for the Percentage
        formPanel.add(percentLabel);
        formPanel.add(percentField);

        // A panel to create space between the previous and the apply button
        formPanel.add(new JLabel());
        formPanel.add(applyButton);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(resultLabel, BorderLayout.SOUTH);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double min = Double.parseDouble(minField.getText());
                    double max = Double.parseDouble(maxField.getText());
                    double percent = Double.parseDouble(percentField.getText());

                    // Takes user inputted values and inputs them as parameter for the updateSalaries in PayrollDAO.java
                    int updatedCount = AdminController.updateSalaryRange(min, max, percent);

                    // Afterwards a display will pop up stating the amount of employees that were affected by the change
                    resultLabel.setText("Updated " + updatedCount + " employees' salaries.");
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Please enter valid numbers.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultLabel.setText("Error applying raise.");
                }
            }
        });

        return panel;
    }
}
