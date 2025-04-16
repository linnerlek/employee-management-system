package app.view;

import app.controller.AdminController;
import app.dao.EmployeeDAO;
import app.model.Employee;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class UpdateEmployeeView {
    private static Map<String, JTextField> fields = new HashMap<>();
    
    public static JDialog createDialog(Employee employee) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Update Employee: " + employee.getFname() + " " + employee.getLname());
        dialog.setModal(true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(null);
        
        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Clear previous fields if any
        fields.clear();
        
        // Personal Information Section
        addSectionLabel(formPanel, "Personal Information", gbc, 0);
        
        addFormField(formPanel, "First Name:", employee.getFname(), "fname", gbc, 1);
        addFormField(formPanel, "Last Name:", employee.getLname(), "lname", gbc, 2);
        addFormField(formPanel, "Email:", employee.getEmail(), "email", gbc, 3);
        addFormField(formPanel, "Phone:", employee.getPhone(), "phone", gbc, 4);
        addFormField(formPanel, "Date of Birth:", employee.getDob(), "dob", gbc, 5);
        addFormField(formPanel, "Gender:", employee.getGender(), "gender", gbc, 6);
        addFormField(formPanel, "Race:", employee.getRace(), "race", gbc, 7);
        
        // Address Section
        addSectionLabel(formPanel, "Address Information", gbc, 8);
        
        addFormField(formPanel, "Street:", employee.getStreet(), "street", gbc, 9);
        addFormField(formPanel, "City:", employee.getCityName(), "city", gbc, 10);
        addFormField(formPanel, "State:", employee.getStateName(), "state", gbc, 11);
        addFormField(formPanel, "ZIP:", employee.getZip(), "zip", gbc, 12);
        
        // Read-only information section
        addSectionLabel(formPanel, "Job Information (Read-only)", gbc, 13);
        
        // These fields are read-only for demonstration purposes
        addReadOnlyField(formPanel, "Employee ID:", String.valueOf(employee.getEmpid()), gbc, 14);
        addReadOnlyField(formPanel, "Job Title:", employee.getJobTitle(), gbc, 15);
        addReadOnlyField(formPanel, "Division:", employee.getDivisionName(), gbc, 16);
        addReadOnlyField(formPanel, "Salary:", employee.getSalary(), gbc, 17);
        addReadOnlyField(formPanel, "Hire Date:", employee.getHireDate(), gbc, 18);
        addReadOnlyField(formPanel, "SSN:", employee.getSsn(), gbc, 19);
        
        // Add form panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Create updated employee object
            Employee updatedEmployee = createUpdatedEmployee(employee);
            
            // Call controller to save changes
            boolean success = AdminController.updateEmployeeData(updatedEmployee);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    "Employee information updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the employee data that was just displayed
                Employee refreshedEmployee = EmployeeDAO.getEmployeeById(employee.getEmpid());
                if (refreshedEmployee != null) {
                    // If this dialog was opened from search results, you might want to refresh those results
                    // For now, we'll just update the current dialog
                    dialog.dispose();
                    
                    // If this was called from the search results, consider refreshing those results
                    // This could be done by passing a callback or using an observer pattern
                } else {
                    // Handle the case where the employee can't be reloaded
                    JOptionPane.showMessageDialog(dialog,
                        "Changes were saved but could not refresh display. Please search again.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update employee information.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        return dialog;
    }
    
    private static void addSectionLabel(JPanel panel, String text, GridBagConstraints gbc, int row) {
        JLabel sectionLabel = new JLabel(text);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(sectionLabel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
    }
    
    private static void addFormField(JPanel panel, String labelText, String initialValue, 
                                    String fieldName, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        
        JTextField textField = new JTextField(initialValue);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(textField, gbc);
        
        // Store field for later retrieval
        fields.put(fieldName, textField);
    }
    
    private static void addReadOnlyField(JPanel panel, String labelText, String value, 
                                        GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
        
        JTextField textField = new JTextField(value);
        textField.setEditable(false);
        textField.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(textField, gbc);
    }
    
    private static Employee createUpdatedEmployee(Employee original) {
        return new Employee(
            original.getEmpid(),
            fields.get("fname").getText(),
            fields.get("lname").getText(),
            fields.get("email").getText(),
            original.getSsn(),
            original.getHireDate(),
            original.getSalary(),
            original.getJobTitle(),
            fields.get("street").getText(),
            fields.get("zip").getText(),
            fields.get("gender").getText(),
            fields.get("race").getText(),
            fields.get("dob").getText(),
            fields.get("phone").getText(),
            fields.get("city").getText(),
            fields.get("state").getText(),
            original.getDivisionName(),
            original.getLastPaidDate()
        );
    }
    
    // Simple validation method for future enhancement
    private static boolean validateInput() {
        // Email validation
        String email = fields.get("email").getText();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "Invalid email format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // DOB validation (YYYY-MM-DD)
        String dob = fields.get("dob").getText();
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Date of Birth must be in format YYYY-MM-DD", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Phone validation (allow various formats for now)
        String phone = fields.get("phone").getText();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Phone number cannot be empty", 
                                         "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    public static void addValidatedFormField(JPanel panel, String labelText, String initialValue, 
                                    String fieldName, GridBagConstraints gbc, int row) {
        addFormField(panel, labelText, initialValue, fieldName, gbc, row);
    
        // Add validation for specific fields
        JTextField field = fields.get(fieldName);
        if (fieldName.equals("email")) {
            field.setToolTipText("Valid email format required");
        } else if (fieldName.equals("dob")) {
            field.setToolTipText("Format: YYYY-MM-DD");
        } else if (fieldName.equals("phone")) {
            field.setToolTipText("Format: XXX-XXX-XXXX");
        }
    }

}