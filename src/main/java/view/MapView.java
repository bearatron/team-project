package view;

import java.awt.*;
import javax.swing.*;

/**
 * The view showing the campus map on the left pane. It is always visible.
 */
public class MapView extends JPanel {
    /**
     * Constructs a MapView, displaying an interactive map with pins representing food trucks.
     */
    public MapView() {
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);
        add(new JLabel("Map goes here", JLabel.CENTER), BorderLayout.CENTER);
    }
}
