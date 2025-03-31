package app.view;

import app.dao.ReportDAO;
import app.dao.PayrollDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboardView {
    // creates a single stat card with title and value
    public static JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(230, 230, 250));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // builds the admin dashboard with org stats
    public static JPanel createAdminCards(JPanel contentPanel) {
        JPanel orgStats = new JPanel(new GridLayout(1, 4, 10, 10));

        int totalEmployees = ReportDAO.getTotalEmployees();
        int totalDivisions = ReportDAO.getTotalDivisions();
        String lastPayrollDate = PayrollDAO.getMostRecentPayrollDate();

        // add stat cards
        orgStats.add(createCard("Total Employees", String.valueOf(totalEmployees)));
        orgStats.add(createCard("Total Divisions", String.valueOf(totalDivisions)));
        orgStats.add(createCard("Last Payroll", lastPayrollDate));

        // add clickable reports link
        JPanel reportsCard = createCard("View Reports", "→");
        reportsCard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportsCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DashboardView.switchPanel(contentPanel, "reports");
            }
        });
        orgStats.add(reportsCard);

        return orgStats;
    }
}