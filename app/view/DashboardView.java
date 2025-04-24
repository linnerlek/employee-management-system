package app.view;

import app.dao.EmployeeDAO;
import app.dao.PayrollDAO;
import app.model.Employee;
import app.model.PayStatement;
import app.model.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class DashboardView {

    private static final Color SIDEBAR_BG = new Color(40, 40, 60);
    private static final Color BTN_DEFAULT = new Color(60, 60, 80);
    private static final Color BTN_HOVER = new Color(80, 80, 100);
    private static final Color BTN_SELECTED = new Color(100, 100, 120);
    private static final Color BTN_TEXT = Color.WHITE;

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
        sidebar.setBackground(SIDEBAR_BG);

        // sidebar buttons
        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnPayHistory = createSidebarButton("Pay History");
        JButton btnSearch = createSidebarButton("Search Employee");
        JButton btnInsert = createSidebarButton("Add Employee");
        JButton btnSalary = createSidebarButton("Raise Salary");
        JButton btnReports = createSidebarButton("Reports");
        JButton btnLogout = createSidebarButton("Logout");

        // buttons for selection logic
        JButton[] navButtons = {btnDashboard, btnPayHistory, btnSearch, btnInsert, btnSalary, btnReports};

        // start with dashboard selected
        setButtonSelected(btnDashboard, true);

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

        // dashboard main panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("Welcome, " + emp.getFname() + " " + emp.getLname(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(user.isAdmin() ? 2 : 1, 1, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        if (user.isAdmin()) {
            JPanel orgStats = AdminDashboardView.createAdminCards(contentPanel, () -> {
                switchPanel(contentPanel, "reports");
                updateButtonSelection(navButtons, btnReports);
            });
            grid.add(orgStats);
        }

        JPanel personalInfo = new JPanel(new GridLayout(0, 2, 10, 10));
        personalInfo.setBorder(BorderFactory.createTitledBorder("My Info"));
        addRow(personalInfo, "Name:", emp.getFname() + " " + emp.getLname());
        addRow(personalInfo, "Job Title:", emp.getJobTitle());
        addRow(personalInfo, "Division:", emp.getDivisionName());
        addRow(personalInfo, "Salary:", emp.getSalary());
        addRow(personalInfo, "Email:", emp.getEmail());
        addRow(personalInfo, "Phone:", emp.getPhone());
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

        contentPanel.add(SearchEmployeeView.create(), "search");
        contentPanel.add(AddEmployeeView.create(), "add");
        contentPanel.add(RaiseSalaryView.create(), "raise");
        contentPanel.add(ReportsView.create(), "reports");

        // add to container
        container.add(sidebar, BorderLayout.WEST);
        container.add(contentPanel, BorderLayout.CENTER);
        frame.setContentPane(container);
        frame.setVisible(true);

        // navigation logic
        btnDashboard.addActionListener(e -> {
            switchPanel(contentPanel, "dashboard");
            updateButtonSelection(navButtons, btnDashboard);
        });
        btnPayHistory.addActionListener(e -> {
            switchPanel(contentPanel, "payHistory");
            updateButtonSelection(navButtons, btnPayHistory);
        });
        btnSearch.addActionListener(e -> {
            switchPanel(contentPanel, "search");
            updateButtonSelection(navButtons, btnSearch);
        });
        btnInsert.addActionListener(e -> {
            switchPanel(contentPanel, "add");
            updateButtonSelection(navButtons, btnInsert);
        });
        btnSalary.addActionListener(e -> {
            switchPanel(contentPanel, "raise");
            updateButtonSelection(navButtons, btnSalary);
        });
        btnReports.addActionListener(e -> {
            switchPanel(contentPanel, "reports");
            updateButtonSelection(navButtons, btnReports);
        });
        btnLogout.addActionListener(e -> {
            frame.dispose();
            LoginView.display();
        });
    }

    private static JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBackground(BTN_DEFAULT);
        button.setForeground(BTN_TEXT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(BTN_SELECTED)) {
                    button.setBackground(BTN_HOVER);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(BTN_SELECTED)) {
                    button.setBackground(BTN_DEFAULT);
                }
            }
        });

        // default border
        button.setBorder(createSidebarButtonBorder(false));
        return button;
    }

    private static void updateButtonSelection(JButton[] buttons, JButton selectedButton) {
        for (JButton button : buttons) {
            setButtonSelected(button, button == selectedButton);
        }
    }

    private static void setButtonSelected(JButton button, boolean selected) {
        button.setBackground(selected ? BTN_SELECTED : BTN_DEFAULT);
        button.setFont(new Font("Arial", selected ? Font.BOLD : Font.PLAIN, 14));
        button.setBorder(createSidebarButtonBorder(selected));
    }

    private static Border createSidebarButtonBorder(boolean selected) {
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        if (selected) {
            Border whiteTab = BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE);
            return BorderFactory.createCompoundBorder(whiteTab, padding);
        } else {
            return padding;
        }
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