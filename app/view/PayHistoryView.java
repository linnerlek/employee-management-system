package app.view;

import app.model.PayStatement;
import app.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PayHistoryView {

    public static JPanel create(User user, List<PayStatement> statements, JPanel contentPanel) {
        JPanel payHistoryPanel = new JPanel(new BorderLayout());

        // set columns based on user type
        String[] columns = user.isAdmin()
                ? new String[]{"EmpID", "Name", "Pay Date", "Earnings", "Deductions", "Net Pay"}
                : new String[]{"Pay Date", "Earnings", "Deductions", "Net Pay"};

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // populate table data
        for (PayStatement ps : statements) {
            double deductions = ps.getTotalDeductions();
            double earnings = ps.getEarnings();
            double netPay = earnings - deductions;

            if (user.isAdmin()) {
                tableModel.addRow(new Object[]{
                        ps.getEmpid(), ps.getName(), ps.getPayDate(), earnings,
                        String.format("%.2f", deductions), String.format("%.2f", netPay)
                });
            } else {
                tableModel.addRow(new Object[]{
                        ps.getPayDate(), earnings,
                        String.format("%.2f", deductions), String.format("%.2f", netPay)
                });
            }
        }

        JTable table = new JTable(tableModel);
        if (user.isAdmin()) {
            table.getColumnModel().getColumn(0).setPreferredWidth(1);
        }

        // custom cell renderer for visual grouping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(null);

                if (user.isAdmin()) {
                    int modelRow = table.convertRowIndexToModel(row);
                    int prevModelRow = modelRow - 1;
                    Object empid = table.getModel().getValueAt(modelRow, 0);
                    Object prevEmpid = prevModelRow >= 0 ? table.getModel().getValueAt(prevModelRow, 0) : null;
                    if (empid != null && !empid.equals(prevEmpid)) {
                        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
                    }
                }
                return c;
            }
        });

        // double-click to view details
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        PayStatement ps = statements.get(row);
                        JPanel reportPanel = PayReportPanel.create(user, ps, () -> DashboardView.switchPanel(contentPanel, "payHistory"));
                        contentPanel.add(reportPanel, "payReport");
                        DashboardView.switchPanel(contentPanel, "payReport");
                    }
                }
            }
        });

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JLabel historyTitle = new JLabel("Pay History");
        historyTitle.setFont(new Font("Arial", Font.BOLD, 20));
        historyTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.add(historyTitle, BorderLayout.CENTER);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 25));
        searchField.setToolTipText("Search by EmpID, Name, DOB, etc.");

        // search filter logic
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText().trim();
                if (text.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        headerPanel.add(titleWrapper, BorderLayout.CENTER);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        payHistoryPanel.add(headerPanel, BorderLayout.NORTH);
        payHistoryPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        return payHistoryPanel;
    }
}