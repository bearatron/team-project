package interface_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the View Manager for the right details panel. Its state is the name of the View which
 * is currently active. An initial state of EMPTY is used.
 */
public class ViewManagerModel {
    private final List<ViewManagerModelListener> listeners;
    private ViewName currentViewName;

    /**
     * Creates a ViewManagerModel with no listeners and EMPTY as the current view.
     */
    public ViewManagerModel() {
        listeners = new ArrayList<>();
        currentViewName = ViewName.EMPTY;
    }

    /**
     * Returns the name of the view that should currently be shown.
     *
     * @return the current view name
     */
    public ViewName getCurrentViewName() {
        return this.currentViewName;
    }

    /**
     * Sets the name of the view that should be shown. This does not notify listeners,
     * so callers must also call {@link #notifyViewChanged()} for the change to take effect.
     *
     * @param currentViewName the name of the view to show
     */
    public void setState(ViewName currentViewName) {
        this.currentViewName = currentViewName;
    }

    /**
     * Registers a listener to be notified when the current view changes.
     *
     * @param listener the listener to add
     */
    public void addListener(ViewManagerModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered listeners that the current view has changed.
     */
    public void notifyViewChanged() {
        for (ViewManagerModelListener listener : listeners) {
            listener.viewChanged();
        }
    }

}
