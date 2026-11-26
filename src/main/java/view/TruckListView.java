package view;

import java.awt.*;
import javax.swing.*;

/**
 * The view that is displayed when no trucks are found.
 */
public class TruckListView extends JPanel {
    /**
     * Constructs a TruckListView object.
     */
    public TruckListView() {
        setLayout(new CardLayout());
        add(new JLabel("No trucks found", JLabel.CENTER), BorderLayout.CENTER);
    }
}
