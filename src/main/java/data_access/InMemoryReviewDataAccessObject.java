package data_access;

import entities.FoodTruck;
import entities.User;
import java.util.HashMap;
import java.util.Map;
import use_case.import_google_reviews.ImportGoogleReviewsDataAccessInterface;
import use_case.post_review.PostReviewDataAccessInterface;

/**
 * Stores users and food trucks in memory for the review use cases, until the
 * team's database is ready.
 */
public class InMemoryReviewDataAccessObject implements PostReviewDataAccessInterface,
        ImportGoogleReviewsDataAccessInterface {
    private final Map<String, User> usersByName = new HashMap<>();
    private final Map<String, FoodTruck> trucksByName = new HashMap<>();
    private final Map<String, FoodTruck> trucksByOwner = new HashMap<>();

    /**
     * Adds a user.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        usersByName.put(user.getUserName(), user);
    }

    /**
     * Adds a food truck, optionally linked to its owner.
     *
     * @param truck the truck to add
     * @param ownerUsername the owner's username, or null if it has no owner
     */
    public void addTruck(FoodTruck truck, String ownerUsername) {
        trucksByName.put(truck.getName(), truck);
        if (ownerUsername != null) {
            trucksByOwner.put(ownerUsername, truck);
        }
    }

    @Override
    public User getUser(String username) {
        return usersByName.get(username);
    }

    @Override
    public FoodTruck getTruck(String truckName) {
        return trucksByName.get(truckName);
    }

    @Override
    public FoodTruck getTruckByOwner(String username) {
        return trucksByOwner.get(username);
    }

    @Override
    public void save(FoodTruck truck) {
        // Trucks are already stored in the maps, so nothing else is needed.
    }
}
