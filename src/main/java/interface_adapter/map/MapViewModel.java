package interface_adapter.map;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel {
    private final List<MapViewModelListener> listeners = new ArrayList<>();
    private interface_adapter.map.MapState state = new MapState();

    public MapState getState() {
        return this.state;
    }

    public void setState(MapState state) {
        this.state = state;
    }

    public void addListener(MapViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (MapViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
