package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewName;
import view.MapView;
import view.NoTrucksView;
import view.ViewManager;

import java.awt.*;
import javax.swing.*;

/**
 * Builds the Swing app.
 * Uses the builder pattern to attach use cases and views.
 */
public class AppBuilder {
    static final double MAP_VIEW_SIZE = 0.6;

    private final JPanel appPanel;

    // the left pane is always a map
    private MapView mapView;

    // the right pane is a stack of cards that the ViewManager switches between
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private NoTrucksView noTrucksView;

    private final ViewManagerModel viewManagerModel;
    private final ViewManager viewManager;

    /**
     * AppBuilder constructor.
     */
    public AppBuilder() {
        appPanel = new JPanel(new GridBagLayout());

        mapView = new MapView();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        viewManagerModel = new ViewManagerModel();
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    }

    /**
     * Adds the view of the list of truck as a card in the right pane.
     *
     * @return this builder
     */
    public AppBuilder addTruckListView() {
        noTrucksView = new NoTrucksView();
        cardPanel.add(noTrucksView, ViewName.TRUCK_LIST.name());
        return this;
    }

    /**
     * Builds the main window.
     *
     * @return the application frame
     */
    public JFrame build() {
        final JFrame application = new JFrame("UofT Food Truck Finder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;

        c.gridx = 0;
        c.weightx = MAP_VIEW_SIZE;
        appPanel.add(mapView, c);

        c.gridx = 1;
        c.weightx = 1 - MAP_VIEW_SIZE;
        appPanel.add(cardPanel, c);

        // 16:9 aspect ratio
        application.setMinimumSize(new Dimension(960, 540));
        application.add(appPanel);

        viewManagerModel.setState(ViewName.TRUCK_LIST);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
