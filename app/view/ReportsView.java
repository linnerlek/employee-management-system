package app.view;

import app.controller.AdminController;
import app.dao.ReportDAO;
import app.model.MonthlyPayRecord;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class ReportsView {
    private static Map<String, Map<String, java.util.List<MonthlyPayRecord>>> groupedDataByJobTitle;
    private static Map<String, Map<String, java.util.List<MonthlyPayRecord>>> groupedDataByDivision;

    public static JPanel create() {
        // Generate data for population
        if(groupedDataByJobTitle == null) {
            java.util.List<MonthlyPayRecord> jobTitleRecords = ReportDAO.getMonthlyPayByJobTitle();
            groupedDataByJobTitle = AdminController.generatePayByJobTitle(jobTitleRecords);
        }

        if (groupedDataByDivision == null) {
            java.util.List<MonthlyPayRecord> divisionRecords = ReportDAO.getMonthlyPayByDivision();
            groupedDataByDivision = AdminController.generatePayByDivision(divisionRecords); 
        }

        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Reports", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Create panel for switching views
        JPanel cardPanel = new JPanel(new CardLayout());

        // Initialize panels
        JPanel buttonsPanel = createButtonPanel(cardPanel);
        buttonsPanel.setName("buttons");
        JPanel yearPanel = createYearPanel(new ArrayList<>(), cardPanel, new HashMap<>()); //Placeholder
        yearPanel.setName("yearPanel");
        JPanel monthPanel = new JPanel(); //Placeholder
        monthPanel.setName("monthPanel");
        
        //Add panels to card panel
        cardPanel.add(buttonsPanel, "buttons");
        cardPanel.add(yearPanel, "yearPanel");
        cardPanel.add(monthPanel, "monthPanel");

        // Add button panel to main panel
        panel.add(cardPanel, BorderLayout.CENTER);

        return panel;
    }

    // Create a panel for view buttons    
    private static JPanel createButtonPanel(JPanel cardPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createVerticalStrut(200));

        buttonPanel.add(createViewButton("View Pay by Job Title", e -> {
            JPanel yearPanel = (JPanel)findComponentByName(cardPanel, "yearPanel");

            // Find position of placeholder year panel
            int index = -1;
            if (yearPanel != null) {
                index = Arrays.asList(cardPanel.getComponents()).indexOf(yearPanel);
            }

            JPanel newYearPanel = createYearPanel(AdminController.getYearList(groupedDataByJobTitle), cardPanel, groupedDataByJobTitle);
            newYearPanel.setName("yearPanel");

            // Replace placeholder
            if (index >= 0) {
                cardPanel.remove(index);
                cardPanel.add(newYearPanel, "yearPanel", index);
            }
            
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "yearPanel");
        }));
        buttonPanel.add(Box.createVerticalStrut(10));

        buttonPanel.add(createViewButton("View Pay by Division", e -> {
            JPanel yearPanel = (JPanel)findComponentByName(cardPanel, "yearPanel");

            int index = -1;
            if (yearPanel != null) {
                index = Arrays.asList(cardPanel.getComponents()).indexOf(yearPanel);
            }

            JPanel newYearPanel = createYearPanel(AdminController.getYearList(groupedDataByDivision), cardPanel, groupedDataByDivision);
            newYearPanel.setName("yearPanel");

            if(index >= 0) {
                cardPanel.remove(index);
                cardPanel.add(newYearPanel, "yearPanel", index);
            }

            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "yearPanel");
            
        }));

        return buttonPanel;
    }

    //Create panel for year drop down menu
    private static JPanel createYearPanel(java.util.List<String> yearList, JPanel cardPanel, Map<String, Map<String, java.util.List<MonthlyPayRecord>>> groupedData) {
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));


        JPanel yearSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        yearSelectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        yearSelectionPanel.add(new JLabel("Select a Year"));

        // Convert list to array for ComboBox population
        String[] yearArray = new String[yearList.size()];
        yearList.toArray(yearArray);

        JComboBox<String> yearBox = new JComboBox<>(yearArray);
        yearBox.setPreferredSize(new Dimension(150, 25));
        yearSelectionPanel.add(yearBox);

        yearPanel.add(yearSelectionPanel);
        yearPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel localButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        localButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add buttons to move to the next panel and back
        JButton nextButton = new JButton("Next");
        JButton backButton = new JButton("Back");

        localButtonPanel.add(nextButton);
        localButtonPanel.add(backButton);

        yearPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        yearPanel.add(localButtonPanel);

        nextButton.addActionListener(e -> {
            String selectedYear = (String) yearBox.getSelectedItem();
            if(selectedYear == null) {
                return;
            }

            // Generate list of months for selected year
            java.util.List<String> monthList = AdminController.getMonthList(groupedData, selectedYear);

            cardPanel.remove(2);
            cardPanel.add(createMonthPanel(monthList, selectedYear, cardPanel, groupedData), "monthPanel");
            
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "monthPanel");
        });

        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "buttons");
        });

        return yearPanel;
    }

    // Create panel for month drop down menu
    private static JPanel createMonthPanel(java.util.List<String> monthList, String selectedYear, JPanel cardPanel, Map<String, Map<String, java.util.List<MonthlyPayRecord>>> groupedData) {
        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.Y_AXIS));
        monthPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel monthSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthSelectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        monthSelectionPanel.add(new JLabel("Select a Month for " + selectedYear));

        String[] monthArray = new String[monthList.size()];
        monthList.toArray(monthArray);

        JComboBox<String> monthBox = new JComboBox<>(monthArray);
        monthBox.setPreferredSize(new Dimension(150, 30));
        monthSelectionPanel.add(monthBox);
        monthPanel.add(monthSelectionPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton viewButton = new JButton("View Report");
        JButton backButton = new JButton("Back");

        buttonPanel.add(viewButton);
        buttonPanel.add(backButton);

        monthPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        monthPanel.add(buttonPanel);

        // Add button to move to data display
        viewButton.addActionListener(e -> {
            String selectedMonth = (String) monthBox.getSelectedItem();

            AdminController.printPayRecord(groupedData, selectedYear, selectedMonth);
        });

        // Add button to move back to year panel
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "yearPanel");
        });

        return monthPanel;
    }

    private static JButton createViewButton(String text, ActionListener action){
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);
        return button;
    }

    private static Component findComponentByName(Container parent, String name) {
        for(Component comp : parent.getComponents()) {
            if(name.equals(comp.getName())) {
                return comp;
            }
        }
        return null;
    }
}
