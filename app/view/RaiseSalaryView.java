package app.view;

import javax.swing.*;
import java.awt.*;

public class RaiseSalaryView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Raise Salary", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Placeholder
        JLabel placeholder = new JLabel("Raise salary form coming soon...", SwingConstants.CENTER);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }
}
