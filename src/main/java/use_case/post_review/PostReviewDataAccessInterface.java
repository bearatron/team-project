package use_case.post_review;

import entities.FoodTruck;
import entities.User;

/**
 * Data access interface for the Post a Review use case.
 */
public interface PostReviewDataAccessInterface {

    /**
     * Returns the user with the given username.
     *
     * @param username the username to look up
     * @return the user, or null if there is no such user
     */
    User getUser(String username);

    /**
     * Returns the food truck with the given name.
     *
     * @param truckName the truck's name, which identifies it
     * @return the truck, or null if there is no such truck
     */
    FoodTruck getTruck(String truckName);

    /**
     * Saves the given food truck, including its reviews.
     *
     * @param truck the food truck to save
     */
    void save(FoodTruck truck);
}
