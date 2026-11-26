package app;

import interface_adapter.AppViewName;
import interface_adapter.ViewManagerModel;
import interface_adapter.details.DetailsViewModel;
import view.*;

import java.awt.*;
import javax.swing.*;

/**
 * Builds the Swing app.
 * Uses the builder pattern to attach use cases and views.
 */
public class AppBuilder {
    static final double MAP_VIEW_SIZE = 0.6;

    private final JPanel appPanel;

    // the left panel is always a map
    private MapView mapView;

    // the right panel is a details view showing a stack of cards that the ViewManager switches between
    private final CardLayout cardLayout;
    private final JPanel detailsViewContainer;
    private DetailsView detailsView;
    private DetailsViewModel detailsViewModel;

    private final ViewManagerModel viewManagerModel;
    private final ViewManager viewManager;

    /**
     * AppBuilder constructor.
     * Adds split pane view with the map on the left the details panel on the right.
     * The right panel being a card layout allows it to switch views.
     */
    public AppBuilder() {
        appPanel = new JPanel(new GridBagLayout());

        mapView = new MapView();

        cardLayout = new CardLayout();
        detailsViewContainer = new JPanel(cardLayout);
        detailsViewModel = new DetailsViewModel();
        JPanel testView = new TestView();
        detailsViewContainer.add(testView);

        detailsView = new DetailsView(detailsViewContainer, cardLayout, detailsViewModel);

        viewManagerModel = new ViewManagerModel();
        viewManager = new ViewManager(detailsViewContainer, cardLayout, viewManagerModel);
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
        appPanel.add(detailsViewContainer, c);

        // 16:9 aspect ratio
        application.setMinimumSize(new Dimension(960, 540));
        application.add(appPanel);

        viewManagerModel.setState(AppViewName.DUAL_PANE);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
