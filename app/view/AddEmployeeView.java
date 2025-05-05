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
        JTextField dobField = new JTextField(); // yyyy-mm-dd
        JTextField phoneField = new JTextField(); // xxx-xxx-xxxx
        JTextField genderField = new JTextField();
        JTextField raceField = new JTextField();
        JTextField hireDateField = new JTextField(); // yyyy-mm-dd
        JTextField salaryField = new JTextField();
        JTextField ssnField = new JTextField();
        JTextArea streetField = new JTextArea(3, 20);
        String[] cities = {"Atlanta", "New York"};
        JComboBox<String> cityComboBox = new JComboBox<>(cities);
        String[] states = {"GA", "NY"};
        JComboBox<String> stateComboBox = new JComboBox<>(states);
        JTextArea countryField = new JTextArea(3, 20);
        JTextArea zipField = new JTextArea(3, 20);
        JTextField jobTitleField = new JTextField();
        String[] divisions = {"HQ", "Technology Engineering", "Human Resources", "Marketing"};
        //JTextField payrollField = new JTextField();
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
        formPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        formPanel.add(dobField);
        formPanel.add(new JLabel("Phone (xxx-xxx-xxxx):"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        formPanel.add(new JLabel("Race:"));
        formPanel.add(raceField);
        formPanel.add(new JLabel("Hire Date (yyyy-mm-dd):"));
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Salary:"));
        formPanel.add(salaryField);
        formPanel.add(new JLabel("SSN:"));
        formPanel.add(ssnField);
        formPanel.add(new JLabel("Street:"));
        formPanel.add(streetField);
        formPanel.add(new JLabel("City:"));
        formPanel.add(cityComboBox);
        formPanel.add(new JLabel("State:"));
        formPanel.add(stateComboBox);
        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);
        formPanel.add(new JLabel("Zipcode:"));
        formPanel.add(zipField);
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
                String dob = dobField.getText().trim();
                String phone = phoneField.getText().trim();
                String gender = genderField.getText().trim();
                String race = raceField.getText().trim();
                LocalDate hireDate = LocalDate.parse(hireDateField.getText().trim());
                BigDecimal salary = new BigDecimal(salaryField.getText().trim());
                String ssn = ssnField.getText().trim();
                String street = streetField.getText().trim();
                String city = (String) cityComboBox.getSelectedItem();
                String state = (String) stateComboBox.getSelectedItem();
                String country = countryField.getText().trim();
                String zip = zipField.getText().trim();
                String jobTitle = jobTitleField.getText().trim();
                String division = (String) divisionComboBox.getSelectedItem();

                // Validation
                if (fname.isEmpty() || lname.isEmpty() || !email.contains("@") || salary.compareTo(BigDecimal.ZERO) <= 0 || jobTitle.isEmpty() || street.isEmpty() || city.isEmpty() || state.isEmpty() || country.isEmpty() || zip.isEmpty()) {
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
                    street,
                    zip,
                    gender,
                    race,
                    dob,
                    phone,
                    city,
                    state,
                    division,
                    LocalDate.now().toString()
                );
                

                
                
                // Submit data
                AdminController controller = new AdminController();
                String result = controller.insertNewEmployee(emp);
                JOptionPane.showMessageDialog(formPanel, result);
                AdminController.insertNewEmployee(emp);
                JOptionPane.showMessageDialog(panel, "Employee added successfully.");


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

        // Mapping division to ID
        public static int mapDivisionToId(String division) {
            switch (division.toLowerCase()) {
                case "hq":
                    return 1;
                case "technology engineering":
                    return 2;
                case "human resources":
                    return 3;
                case "marketing":
                    return 4;
                default:
                    throw new IllegalArgumentException("Invalid division.");
            }
        }

    

        public static int mapJobTitleToId(String jobTitle) { 
            switch (jobTitle.toLowerCase()) {
                case "software manager":
                    return 100;
                case "software architect":
                    return 101;
                case "software engineer":
                return 102;
                case "software developer":
                return 103;
                case "marketing manager":
                return 200;
                case "marketing associate":
                return 201;
                case "marketing assistant":
                return 202;
                case "chief exec. officer":
                return 900;
                case "chief finn. officer":
                return 901;
                case "chief info. officer":
                return 902;
                default:
                throw new IllegalArgumentException("Invalid job title.");
            }
        }

        public static int mapCityToId(String city) {
            switch (city.toLowerCase()) {
                case "atlanta":
                    return 1;
                case "new york":
                    return 2;
                default:
                    throw new IllegalArgumentException("Invalid city.");
            }
        }
        public static int mapStateToId(String state) {
            switch (state.toLowerCase()) {
                case "ga":
                    return 1;
                case "ny":
                    return 2;
                default:
                    throw new IllegalArgumentException("Invalid state.");
            }
        }
}
