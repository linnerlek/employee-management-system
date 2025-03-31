package app.view;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeView {
    public static JPanel create() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Add New Employee", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        // Placeholder
        JLabel placeholder = new JLabel("Add employee form coming soon...", SwingConstants.CENTER);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }
}
