package view;

import java.awt.*;
import javax.swing.*;

/**
 * The view that is displayed when no trucks are found.
 */
public class NoTrucksView extends JPanel {
    /**
     * Constructs a NoTrucksView object.
     */
    public NoTrucksView() {
        setLayout(new CardLayout());
        add(new JLabel("No trucks found", JLabel.CENTER), BorderLayout.CENTER);
    }
}
