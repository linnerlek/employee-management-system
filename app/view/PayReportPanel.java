package app.view;

import app.model.PayStatement;
import app.model.User;
import app.model.Employee;
import app.dao.EmployeeDAO;

import javax.swing.*;
import java.awt.*;

public class PayReportPanel {

    public static JPanel create(User user, PayStatement ps, Runnable onBack) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Paycheck for " + ps.getPayDate(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        // employee info section
        JPanel empInfo = new JPanel(new GridLayout(0, 2, 10, 10));
        empInfo.setBorder(BorderFactory.createTitledBorder("Employee Info"));

        Employee emp = user.getEmployee();

        if (emp != null) {
            addRow(empInfo, "Name:", emp.getFname() + " " + emp.getLname());
            addRow(empInfo, "Email:", emp.getEmail());
            addRow(empInfo, "Job Title:", emp.getJobTitle());
            addRow(empInfo, "Division:", emp.getDivisionName());
        }
        addRow(empInfo, "Pay Date:", ps.getPayDate());
        panel.add(empInfo, BorderLayout.NORTH);

        // earnings and deductions
        JPanel breakdown = new JPanel(new GridLayout(1, 2, 40, 10));

        // earnings panel
        JPanel earningsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        earningsPanel.setBorder(BorderFactory.createTitledBorder("Earnings"));
        earningsPanel.add(new JLabel("Earnings: " + format(ps.getEarnings())));
        breakdown.add(earningsPanel);

        // deductions panel
        JPanel deductionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        deductionsPanel.setBorder(BorderFactory.createTitledBorder("Deductions"));
        deductionsPanel.add(new JLabel("Federal Tax: " + format(ps.getFedTax())));
        deductionsPanel.add(new JLabel("Medicare: " + format(ps.getFedMed())));
        deductionsPanel.add(new JLabel("Social Security: " + format(ps.getFedSS())));
        deductionsPanel.add(new JLabel("State Tax: " + format(ps.getStateTax())));
        deductionsPanel.add(new JLabel("401k Retirement: " + format(ps.getRetire401k())));
        deductionsPanel.add(new JLabel("Health Care: " + format(ps.getHealthCare())));

        double totalDeductions = ps.getTotalDeductions();
        deductionsPanel.add(new JLabel("Total Deductions: " + format(totalDeductions)));

        breakdown.add(deductionsPanel);
        panel.add(breakdown, BorderLayout.CENTER);

        // net pay display
        JPanel netPayPanel = new JPanel(new BorderLayout());
        JLabel netPayLabel = new JLabel("Net Pay: " + format(ps.getNetPay()), SwingConstants.RIGHT);
        netPayLabel.setFont(new Font("Arial", Font.BOLD, 22));
        netPayLabel.setForeground(new Color(0, 102, 0));
        netPayPanel.add(netPayLabel, BorderLayout.EAST);
        panel.add(netPayPanel, BorderLayout.SOUTH);

        // back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> onBack.run());
        netPayPanel.add(backButton, BorderLayout.WEST);

        return panel;
    }

    // adds label-value pair to panel
    private static void addRow(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }

    // formats money values
    private static String format(double val) {
        return String.format("$%.2f", val);
    }
}