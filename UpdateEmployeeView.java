package app.view;

import app.controller.AdminController;
import app.model.Employee;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class UpdateEmployeeView {
    private static Map<String, JTextField> fields = new HashMap<>();
    private static Map<String, String> originalValues = new HashMap<>();
    
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
        originalValues.clear();
        
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
        
        // Job information section
        addSectionLabel(formPanel, "Job Information", gbc, 13);
        
        addReadOnlyField(formPanel, "Employee ID:", String.valueOf(employee.getEmpid()), gbc, 14);
        addFormField(formPanel, "Job Title:", employee.getJobTitle(), "jobTitle", gbc, 15);
        addFormField(formPanel, "Division:", employee.getDivisionName(), "divisionName", gbc, 16);
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
            // First validate input
            if (!validateInput()) {
                return; // Stop if validation fails
            }
            
            // Create updated employee object
            Employee updatedEmployee = createUpdatedEmployee(employee);
            
            if (updatedEmployee == null) {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to create updated employee. Please check your input.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Call controller to save changes
            boolean success = AdminController.updateEmployeeData(updatedEmployee);
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, 
                    "Employee information updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // If this dialog was opened from search results, you want to refresh those results
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update employee information. Please try again later.", 
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

        // Handle null or "Missing" values appropriately
        String displayValue = (initialValue == null || initialValue.equals("Missing")) ? "" : initialValue;
        
        JTextField textField = new JTextField(displayValue);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(textField, gbc);
        
        // Store initial value
        originalValues.put(fieldName, initialValue);
        
        // Store field for later retrieval
        fields.put(fieldName, textField);
        
        // Add tooltips based on field type
        if (fieldName.equals("email")) {
            textField.setToolTipText("Format: (example@domain.com)");
        } else if (fieldName.equals("dob")) {
            textField.setToolTipText("Format: YYYY-MM-DD");
        } else if (fieldName.equals("phone")) {
            textField.setToolTipText("Format: XXX-XXX-XXXX");
        } else if (fieldName.equals("zip")) {
            textField.setToolTipText("Format: 12345 or 12345-6789");
        }
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
        // Create new Employee with updated fields from the form
        return new Employee(
            original.getEmpid(),
            getValueOrOriginal("fname", original.getFname()),
            getValueOrOriginal("lname", original.getLname()),
            getValueOrOriginal("email", original.getEmail()),
            original.getSsn(),
            original.getHireDate(),
            original.getSalary(),
            getValueOrOriginal("jobTitle", original.getJobTitle()),
            getValueOrOriginal("street", original.getStreet()),
            getValueOrOriginal("zip", original.getZip()),
            getValueOrOriginal("gender", original.getGender()),
            getValueOrOriginal("race", original.getRace()),
            getValueOrOriginal("dob", original.getDob()),
            getValueOrOriginal("phone", original.getPhone()),
            getValueOrOriginal("city", original.getCityName()),
            getValueOrOriginal("state", original.getStateName()),
            getValueOrOriginal("divisionName", original.getDivisionName()),
            original.getLastPaidDate()
        );
    }

    // Helper method to get the form value or the original value if form is empty
    private static String getValueOrOriginal(String fieldName, String originalValue) {
        String fieldValue = fields.get(fieldName).getText().trim();
        if (fieldValue.isEmpty()) {
            // Don't return "Missing" for date fields - use empty string instead
            if ((fieldName.equals("dob") || fieldName.equals("phone") || fieldName.equals("gender") || 
                fieldName.equals("race") || fieldName.equals("zip") || fieldName.equals("street") ||
                fieldName.equals("city") || fieldName.equals("state")) && 
                (originalValue == null || originalValue.equals("Missing"))) {
                return "";
            }
            return originalValue;
        }
        return fieldValue;
    }
    
    // Validation method that handles unchanged fields
    private static boolean validateInput() {
        // Basic validation for required fields
        String fname = fields.get("fname").getText().trim();
        if (fname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "First name cannot be empty", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String lname = fields.get("lname").getText().trim();
        if (lname.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Last name cannot be empty", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Email validation
        String email = fields.get("email").getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(null, "Invalid email format", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Phone validation - only validate if changed
        String phone = fields.get("phone").getText().trim();
        
        // If phone is not empty and has been changed, validate the format
        if (!phone.isEmpty() && !phone.matches("\\d{3}-\\d{3}-\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Phone must be in format XXX-XXX-XXXX", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // DOB validation - only validate if changed
        String dob = fields.get("dob").getText().trim();
      
        // If DOB is not empty and has been changed, validate the format
        if (!dob.isEmpty() && !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Date of Birth must be in format YYYY-MM-DD", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // ZIP code validation - only validate if changed
        String zip = fields.get("zip").getText().trim();
       
        // If ZIP is not empty and has been changed, validate the format
        if (!zip.isEmpty() && !zip.matches("\\d{5}(-\\d{4})?")) {
            JOptionPane.showMessageDialog(null, "ZIP code must be in format 12345 or 12345-6789", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        return true;
    }
}