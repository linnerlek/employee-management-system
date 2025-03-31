package app.view;

import javax.swing.*;
import java.awt.*;

public class SearchEmployeeView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Search Employee", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Placeholder for actual search implementation
        JLabel placeholder = new JLabel("Search functionality coming soon...", SwingConstants.CENTER);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }
}
