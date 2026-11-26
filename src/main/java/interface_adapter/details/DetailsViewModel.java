package interface_adapter.details;

import interface_adapter.AppViewName;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel {
    private final List<DetailsViewModelListener> listeners = new ArrayList<>();
    private DetailsState state = new DetailsState();

    public DetailsState getState() {
        return this.state;
    }

    public void setState(DetailsState state) {
        this.state = state;
    }

    public void addListener(DetailsViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (DetailsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
