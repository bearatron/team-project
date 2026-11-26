package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.AppViewName;
import interface_adapter.map.MapState;
import interface_adapter.map.MapViewModel;
import interface_adapter.map.MapViewModelListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The view showing the campus map on the left pane. It is always visible.
 */
public class MapView extends JPanel implements MapViewModelListener {
    private MapState mapState;
    private MapViewModel mapViewModel;
    private final JButton incrementBtn;
    private final JButton decrementBtn;

    /**
     * Constructs a MapView, displaying an interactive map with pins representing food trucks.
     */
    public MapView() {
        mapViewModel = new MapViewModel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.lightGray);
        JLabel label = new JLabel("Map goes here", JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        // state change to be implemented
        incrementBtn = new JButton("increment");
        incrementBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        decrementBtn = new JButton("decrement");
        decrementBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(label);
        add(incrementBtn);
        add(decrementBtn);
        add(Box.createVerticalGlue());

    }

    @Override
    public void stateChanged() {}
}
