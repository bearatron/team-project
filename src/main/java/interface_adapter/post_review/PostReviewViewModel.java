package interface_adapter.post_review;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The view model for the Post a Review view.
 */
public class PostReviewViewModel {
    public static final String VIEW_NAME = "post review";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private PostReviewState state = new PostReviewState();

    /**
     * Returns the current state.
     *
     * @return the current state
     */
    public PostReviewState getState() {
        return state;
    }

    /**
     * Replaces the current state.
     *
     * @param state the new state
     */
    public void setState(PostReviewState state) {
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
