import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class EmployeeForm {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeForm().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Add New Employee");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 2, 5, 5));

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

        // Adding fields to panel
        panel.add(new JLabel("Employee ID:"));
        panel.add(empIdField);
        panel.add(new JLabel("First Name:"));
        panel.add(fNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Hire Date (yyyy-mm-dd):"));
        panel.add(hireDateField);
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("SSN:"));
        panel.add(ssnField);
        panel.add(new JLabel("Address:"));
        panel.add(new JScrollPane(addressArea));
        panel.add(new JLabel("Job Title:"));
        panel.add(jobTitleField);
        panel.add(new JLabel("Division:"));
        panel.add(divisionComboBox);

        // Submit button
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

        submitButton.addActionListener(e -> {
            try {
                // Read input
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

                // Basic validation
                if (fname.isEmpty() || lname.isEmpty() || !email.contains("@") || 
                    salary.compareTo(BigDecimal.ZERO) <= 0 || jobTitle.isEmpty() || address.isEmpty()) {
                    throw new IllegalArgumentException("Validation failed: Check all required fields.");
                }

                // Map job title and division to corresponding IDs
                int jobTitleId = mapJobTitleToId(jobTitle);
                int divisionId = mapDivisionToId(division);

                employeeData data = new employeeData();
                data.setEmpid(empId);
                data.setFname(fname);
                data.setLname(lname);
                data.setEmail(email);
                data.setHireDate(hireDate);
                data.setSalary(salary);
                data.setSsn(ssn);
                data.setAddress(address); // Ensure this setter exists
                data.setJobTitle(jobTitle); // Set job title name
                data.setJobTitleId(jobTitleId); // Set job title ID
                data.setDivisionId(divisionId); // Set division ID

                // Submit data
                AdminController controller = new AdminController();
                String result = controller.insertEmployee(data);
                JOptionPane.showMessageDialog(frame, result);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid number format.");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use yyyy-mm-dd.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // Mapping job title to ID
    private int mapJobTitleToId(String jobTitle) {
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

    // Mapping division to ID
    private int mapDivisionToId(String division) {
        switch (division.toLowerCase()) {
            case "hr":
                return 3;
            case "it":
                return 1;
            case "finance":
                return 2;
            case "marketing":
                return 4;
            case "operations":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid division.");
        }
    }
}
