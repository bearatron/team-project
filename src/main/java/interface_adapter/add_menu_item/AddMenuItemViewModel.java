package interface_adapter.add_menu_item;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The view model for the Add Menu Item view.
 */
public class AddMenuItemViewModel {
    public static final String VIEW_NAME = "add menu item";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private AddMenuItemState state = new AddMenuItemState();

    /**
     * Returns the current state.
     *
     * @return the current state
     */
    public AddMenuItemState getState() {
        return state;
    }

    /**
     * Replaces the current state.
     *
     * @param state the new state
     */
    public void setState(AddMenuItemState state) {
        this.state = state;
    }

    /**
     * Notifies listeners (the view) that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /**
     * Registers a listener (the view) to be told about state changes.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}