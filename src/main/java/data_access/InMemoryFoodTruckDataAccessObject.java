package data_access;

import entities.FoodTruck;
import java.util.HashMap;
import java.util.Map;
import use_case.add_menu_item.AddMenuItemDataAccessInterface;

/**
 * Stores food trucks in memory, keyed by owner username.
 */
public class InMemoryFoodTruckDataAccessObject implements AddMenuItemDataAccessInterface {
    private final Map<String, FoodTruck> trucksByOwner = new HashMap<>();

    /**
     * Registers a truck for the given owner.
     *
     * @param username the owner's username
     * @param truck the owner's truck
     */
    public void addTruck(String username, FoodTruck truck) {
        trucksByOwner.put(username, truck);
    }

    @Override
    public FoodTruck getTruckByOwner(String username) {
        return trucksByOwner.get(username);
    }

    @Override
    public void save(FoodTruck truck) {
        // Trucks are already stored in the map, so nothing else is needed.
    }
}