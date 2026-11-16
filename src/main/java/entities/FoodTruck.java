package entities;

import java.util.ArrayList;
import java.util.List;

public class FoodTruck {
    private final User owner;
    private final List<Review> reviews;
    private final List<String> cuisineTypes;
    private final Menu menu;
    private final Location location;

    public FoodTruck(User owner) {
        if (owner.getType() != "owner") {
            // TODO: change to actual User get type implementation
            throw new IllegalArgumentException("User's account must be classified as an owner");
        }

        this.owner = owner;
        reviews = new ArrayList<>();
        cuisineTypes = new ArrayList<>();
        menu = new Menu();
        location = null;
    }

    public User getOwner() {
        return owner;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<String> getCuisineTypes() {
        return cuisineTypes;
    }

    public Menu getMenu() {
        return menu;
    }

    public Location getLocation() {
        return location;
    }
}
