package interface_adapter;

/**
 * Template for ViewManagerModelListener (whose implementation is ViewManager)
 */
public interface ViewManagerModelListener {

    /**
     * Update which view is currently being displayed.
     */
    void viewChanged();
}
