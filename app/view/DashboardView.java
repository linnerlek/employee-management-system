package app.view;

import app.dao.EmployeeDAO;
import app.dao.PayrollDAO;
import app.model.Employee;
import app.model.PayStatement;
import app.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardView {

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

        // main container
        JPanel contentPanel = new JPanel(new CardLayout());
        JPanel container = new JPanel(new BorderLayout());

        // nav menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(180, 600));
        sidebar.setBackground(new Color(40, 40, 60));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnPayHistory = new JButton("Pay History");
        JButton btnSearch = new JButton("Search Employee");
        JButton btnInsert = new JButton("Add Employee");
        JButton btnSalary = new JButton("Raise Salary");
        JButton btnReports = new JButton("Reports");
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

        // main dashboard view
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + emp.getFname() + " " + emp.getLname(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(user.isAdmin() ? 2 : 1, 1, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        if (user.isAdmin()) {
            JPanel orgStats = AdminDashboardView.createAdminCards(contentPanel);
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
        String address = String.join(", ", emp.getStreet(), emp.getCityName(), emp.getStateName(), emp.getZip());
        addRow(personalInfo, "Address:", address);

        grid.add(personalInfo);
        dashboardPanel.add(grid, BorderLayout.CENTER);
        contentPanel.add(dashboardPanel, "dashboard");

        // load pay history
        List<PayStatement> statements = user.isAdmin()
            ? PayrollDAO.getAllPayStatements()
            : PayrollDAO.getPayStatements(user.getEmpid());
        JPanel payHistoryPanel = PayHistoryView.create(user, statements, contentPanel);
        contentPanel.add(payHistoryPanel, "payHistory");

        // setup other views
        contentPanel.add(SearchEmployeeView.create(), "search");
        contentPanel.add(AddEmployeeView.create(), "add");
        contentPanel.add(RaiseSalaryView.create(), "raise");
        contentPanel.add(ReportsView.create(), "reports");

        container.add(sidebar, BorderLayout.WEST);
        container.add(contentPanel, BorderLayout.CENTER);
        frame.setContentPane(container);
        frame.setVisible(true);

        // navigation logic
        btnDashboard.addActionListener(e -> switchPanel(contentPanel, "dashboard"));
        btnPayHistory.addActionListener(e -> switchPanel(contentPanel, "payHistory"));
        btnSearch.addActionListener(e -> switchPanel(contentPanel, "search"));
        btnInsert.addActionListener(e -> switchPanel(contentPanel, "add"));
        btnSalary.addActionListener(e -> switchPanel(contentPanel, "raise"));
        btnReports.addActionListener(e -> switchPanel(contentPanel, "reports"));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            LoginView.display();
        });
    }

    public static void switchPanel(JPanel contentPanel, String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }
    
    // adds a label-value pair to panel
    private static void addRow(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value));
    }

    // hides most of ssn for security
    private static String maskSSN(String ssn) {
        return (ssn != null && ssn.length() == 11) ? "***-**-" + ssn.substring(7) : "Missing";
    }
}