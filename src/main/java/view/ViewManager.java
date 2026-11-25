package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

import java.awt.*;
import javax.swing.*;

/**
 * The View Manager for the right details panel. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements ViewManagerModelListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates a ViewManager that switches the visible card in the given panel
     * whenever the given model's current view changes.
     * Registers itself as a listener on the model.
     *
     * @param views the panel that holds one card per view
     * @param cardLayout the layout of {@code views}, used to show the selected card
     * @param viewManagerModel the model whose current view name determines which card is shown
     */
    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    @Override
    public void viewChanged() {
        cardLayout.show(views, viewManagerModel.getCurrentViewName().toString());
    }
}
