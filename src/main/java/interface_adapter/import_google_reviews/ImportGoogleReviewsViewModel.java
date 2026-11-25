package interface_adapter.import_google_reviews;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The view model for the Import Google Maps Reviews view.
 */
public class ImportGoogleReviewsViewModel {
    public static final String VIEW_NAME = "import google reviews";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private ImportGoogleReviewsState state = new ImportGoogleReviewsState();

    /**
     * Returns the current state.
     *
     * @return the current state
     */
    public ImportGoogleReviewsState getState() {
        return state;
    }

    /**
     * Replaces the current state.
     *
     * @param state the new state
     */
    public void setState(ImportGoogleReviewsState state) {
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
