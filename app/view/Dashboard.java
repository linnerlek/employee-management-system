package app.view;

import app.model.User;
import app.model.Employee;
import app.dao.EmployeeDAO;
import app.dao.PayrollDAO;
import app.dao.ReportDAO;

import javax.swing.*;
import java.awt.*;

public class Dashboard {
    public static void display(User user) {
        JFrame frame = new JFrame("Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        Employee emp = EmployeeDAO.getEmployeeById(user.getEmpid());
        if (emp == null) {
            JOptionPane.showMessageDialog(frame, "Failed to load employee data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(180, 600));
        sidebar.setBackground(new Color(40, 40, 60));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnSearch = new JButton("Search Employee");
        JButton btnInsert = new JButton("Add Employee");
        JButton btnSalary = new JButton("Raise Salary");
        JButton btnReports = new JButton("Reports");
        JButton btnPayHistory = new JButton("Pay History");
        JButton btnLogout = new JButton("Logout");

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnDashboard);
        sidebar.add(btnPayHistory);
        if (user.isAdmin()) {
            sidebar.add(btnSearch);
            sidebar.add(btnInsert);
            sidebar.add(btnSalary);
            sidebar.add(btnReports);
        }
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        JPanel contentPanel = new JPanel(new CardLayout());

        JPanel dashboardPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + emp.getFname() + " " + emp.getLname(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(user.isAdmin() ? 2 : 1, 1, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        if (user.isAdmin()) {
            JPanel orgStats = new JPanel(new GridLayout(1, 4, 10, 10));

            int totalEmployees = ReportDAO.getTotalEmployees();
            int totalDivisions = ReportDAO.getTotalDivisions();
            String lastPayrollDate = PayrollDAO.getMostRecentPayrollDate();

            orgStats.add(createCard("Total Employees", String.valueOf(totalEmployees)));
            orgStats.add(createCard("Total Divisions", String.valueOf(totalDivisions)));
            orgStats.add(createCard("Last Payroll", lastPayrollDate));
            orgStats.add(createCard("View Reports", "→"));

            grid.add(orgStats);
        }

        JPanel personalInfo = new JPanel(new GridLayout(0, 2, 10, 10));
        personalInfo.setBorder(BorderFactory.createTitledBorder("My Info"));

        addRow(personalInfo, "Name:", emp.getFname() + " " + emp.getLname());
        addRow(personalInfo, "Email:", emp.getEmail());
        addRow(personalInfo, "Phone:", emp.getPhone());
        addRow(personalInfo, "Job Title:", emp.getJobTitle());
        addRow(personalInfo, "Division:", emp.getDivisionName());
        addRow(personalInfo, "Salary:", emp.getSalary());
        addRow(personalInfo, "Hire Date:", emp.getHireDate());
        addRow(personalInfo, "DOB:", emp.getDob());
        addRow(personalInfo, "SSN:", maskSSN(emp.getSsn()));

        String fullAddress = String.join(", ",
                emp.getStreet(), emp.getCityName(), emp.getStateName(), emp.getZip());
        addRow(personalInfo, "Address:", fullAddress);

        grid.add(personalInfo);
        dashboardPanel.add(grid, BorderLayout.CENTER);
        contentPanel.add(dashboardPanel, "dashboard");

        JPanel container = new JPanel(new BorderLayout());
        container.add(sidebar, BorderLayout.WEST);
        container.add(contentPanel, BorderLayout.CENTER);

        frame.setContentPane(container);
        frame.setVisible(true);

        // Navigation
        btnDashboard.addActionListener(e -> switchPanel(contentPanel, "dashboard"));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            LoginView.display();
        });
        btnPayHistory.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Pay report view coming soon!", "Pay History", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static void switchPanel(JPanel contentPanel, String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    private static JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(230, 230, 250));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private static void addRow(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }

    private static String maskSSN(String ssn) {
        return (ssn != null && ssn.length() == 11) ? "***-**-" + ssn.substring(7) : "Missing";
    }
}
