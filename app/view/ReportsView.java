package app.view;

import app.controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class ReportsView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Reports", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 10, 10));

        // Add Full Employee Report button
        JButton fullReportButton = new JButton("Full Employee Report");
        fullReportButton.addActionListener(e -> AdminController.generateFullReport());
        buttonPanel.add(fullReportButton);

        // Add spacing and padding
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(buttonPanel);
        panel.add(centerWrapper, BorderLayout.CENTER);

        return panel;
    }
}
