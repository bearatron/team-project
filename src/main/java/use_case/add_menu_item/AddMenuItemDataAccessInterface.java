package use_case.add_menu_item;

import entities.FoodTruck;

/**
 * Data access interface for the Add Menu Item use case.
 */
public interface AddMenuItemDataAccessInterface {

    /**
     * Returns the food truck owned by the given user.
     *
     * @param username the owner's username
     * @return the owner's food truck, or null if they have no truck
     */
    FoodTruck getTruckByOwner(String username);

    /**
     * Saves the given food truck.
     *
     * @param truck the food truck to save
     */
    void save(FoodTruck truck);
}