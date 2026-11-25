package use_case.import_google_reviews;

import entities.FoodTruck;
import entities.User;

/**
 * Data access interface for the Import Google Maps Reviews use case.
 */
public interface ImportGoogleReviewsDataAccessInterface {

    /**
     * Returns the user with the given username.
     *
     * @param username the username to look up
     * @return the user, or null if there is no such user
     */
    User getUser(String username);

    /**
     * Returns the food truck owned by the given user.
     *
     * @param username the owner's username
     * @return the owner's food truck, or null if they have no truck
     */
    FoodTruck getTruckByOwner(String username);

    /**
     * Saves the given food truck, including its reviews.
     *
     * @param truck the food truck to save
     */
    void save(FoodTruck truck);
}
