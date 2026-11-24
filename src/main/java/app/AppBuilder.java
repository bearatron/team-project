package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewName;
import view.ViewManager;

import java.awt.*;
import javax.swing.*;

public class AppBuilder {
    private final JPanel appPanel;
    final ViewManagerModel viewManagerModel;
    final ViewManager viewManager;

    public AppBuilder() {
        appPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        appPanel.setLayout(cardLayout);
        viewManagerModel = new ViewManagerModel();
        viewManager = new ViewManager(appPanel, cardLayout, viewManagerModel);
    }

    public JFrame build() {
        final JFrame application = new JFrame("UofT Food Truck Finder");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 16:9 aspect ratio
        application.setMinimumSize(new Dimension(960, 540));
        application.add(appPanel);

        viewManagerModel.setState(ViewName.FOOD_TRUCK_LIST);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
