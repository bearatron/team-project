package interface_adapter.map;

import org.apache.commons.lang3.Validate;

import java.util.Map;

public class MapState {
    private int selectedTruck = -1;
    private Map<Integer, String> idToTruck;

    public int getSelectedTruck() {
        return selectedTruck;
    }

    public void setSelectedTruck(int selectedTruck) {
        this.selectedTruck = selectedTruck;
    }

    public Map<Integer, String> getIdToTruck() {
        return idToTruck;
    }

    /**
     * {@code idToTruck} map setter.
     * ids should be positive, since "no truck selected" is represented by -1
     *
     * @param idToTruck map from non-negative truck id to truck name
     */
    public void setIdToTruck(Map<Integer, String> idToTruck) {
        Validate.isTrue(idToTruck.keySet().stream().allMatch(id -> id >= 0));
        this.idToTruck = idToTruck;
    }
}
