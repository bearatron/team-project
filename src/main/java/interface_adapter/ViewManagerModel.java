package interface_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of EMPTY is used.
 */
public class ViewManagerModel {
    private final List<ViewManagerModelListener> listeners;
    private ViewName currentViewName;

    public ViewManagerModel() {
        listeners = new ArrayList<>();
        currentViewName = ViewName.EMPTY;
    }

    public ViewName getCurrentViewName() {
        return this.currentViewName;
    }

    public void setState(ViewName currentViewName) {
        this.currentViewName = currentViewName;
    }

    public void addListener(ViewManagerModelListener listener) {
        listeners.add(listener);
    }

    public void notifyViewChanged() {
        for (ViewManagerModelListener listener : listeners) {
            listener.viewChanged();
        }
    }

}
