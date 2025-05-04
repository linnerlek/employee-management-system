package app.view;

import app.controller.AdminController;
import app.model.Employee;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class AddEmployeeView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Add New Employee", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(12, 2, 5, 5));

        // Input fields
        JTextField empIdField = new JTextField();
        JTextField fNameField = new JTextField();
        JTextField lNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField hireDateField = new JTextField(); // yyyy-mm-dd
        JTextField salaryField = new JTextField();
        JTextField ssnField = new JTextField();
        JTextArea addressArea = new JTextArea(3, 20);
        JTextField jobTitleField = new JTextField();
        String[] divisions = {"HR", "IT", "Finance", "Marketing", "Operations"};
        JComboBox<String> divisionComboBox = new JComboBox<>(divisions);

        // Add fields to panel
        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(empIdField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(fNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Hire Date (yyyy-mm-dd):"));
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Salary:"));
        formPanel.add(salaryField);
        formPanel.add(new JLabel("SSN:"));
        formPanel.add(ssnField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(new JScrollPane(addressArea));
        formPanel.add(new JLabel("Job Title:"));
        formPanel.add(jobTitleField);
        formPanel.add(new JLabel("Division:"));
        formPanel.add(divisionComboBox);

        // Submit button
        JButton submitButton = new JButton("Submit");
        formPanel.add(new JLabel()); // Empty cell
        formPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(empIdField.getText().trim());
                String fname = fNameField.getText().trim();
                String lname = lNameField.getText().trim();
                String email = emailField.getText().trim();
                LocalDate hireDate = LocalDate.parse(hireDateField.getText().trim());
                BigDecimal salary = new BigDecimal(salaryField.getText().trim());
                String ssn = ssnField.getText().trim();
                String address = addressArea.getText().trim();
                String jobTitle = jobTitleField.getText().trim();
                String division = (String) divisionComboBox.getSelectedItem();

                // Validation
                if (fname.isEmpty() || lname.isEmpty() || !email.contains("@") || salary.compareTo(BigDecimal.ZERO) <= 0 || jobTitle.isEmpty() || address.isEmpty()) {
                    throw new IllegalArgumentException("Validation failed: Check all required fields.");
                }

                Employee emp = new Employee(
                    empId,
                    fname,
                    lname,
                    email,
                    ssn,
                    hireDate.toString(),
                    salary.toPlainString(),
                    jobTitle,
                    "N/A",         // street
                    "00000",       // zip
                    "N/A",         // gender
                    "N/A",         // race
                    "1900-01-01",  // dob
                    "000-000-0000",// phone
                    "N/A",         // cityName
                    "N/A",         // stateName
                    division,
                    LocalDate.now().toString() // lastPaidDate
                );


                String result = AdminController.insertNewEmployee(emp);
                JOptionPane.showMessageDialog(panel, result);


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid number format.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid date format. Use yyyy-mm-dd.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
}
