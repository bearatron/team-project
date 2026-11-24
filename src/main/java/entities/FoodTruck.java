package entities;

import java.awt.Image;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a FoodTruck on the UofT campus.
 */
public class FoodTruck {
    private final String name;
    private Location location;
    private final List<String> cuisineType;
    private final List<Image> photos;
    private final List<MenuItem> menu = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();

    /**
     * Creates a new FoodTruck with the given name, location, cuisine, and
     * optional photos to be uploaded.
     *
     * @param name The name of the FoodTruck.
     * @param location The location of the FoodTruck.
     * @param cuisineType The type of cuisine of the FoodTruck.
     * @param photos List of optional images related to the FoodTruck.
     */
    public FoodTruck(String name,
                     Location location,
                     List<String> cuisineType,
                     List<Image> photos) {
        this.name = name;
        this.location = location;
        this.cuisineType = new ArrayList<>(cuisineType);
        this.photos = new ArrayList<>(photos);
    }

    /**
     * Add a MenuItem to the given menu of a FoodTruck.
     *
     * @param item the menu item to add
     */
    public void addItem(MenuItem item) {
        menu.add(item);
    }

    /**
     * Add a review from a user to a given FoodTruck.
     *
     * @param review the review to add
     */
    public void addReview(Review review) {
        reviews.add(review);
    }

    /**
     * Returns the range of prices across all items on the FoodTruck's menu.
     *
     * @return a PriceRange of the highest and lowest prices on the menu,
     *      or null, if there is no menu.
     */
    public PriceRange getPriceRange() {
        if (menu.isEmpty()) {
            return null;
        }
        BigDecimal min = menu.stream().map(MenuItem::getPrice)
                .min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        BigDecimal max = menu.stream().map(MenuItem::getPrice)
                .max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        return new PriceRange(min, max);
    }

    /**
     * Returns the name of a FoodTruck.
     *
     * @return the name of the FoodTruck.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location of the FoodTruck.
     *
     * @return the location of the FoodTruck.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Updates this food truck's location.
     *
     * @param newLocation the new location
     */
    public void updateLocation(Location newLocation) {
        this.location = newLocation;
    }
    /**
     * Returns the CuisineType of the FoodTruck.
     *
     * @return the CuisineType of the FoodTruck.
     */
    public List<String> getCuisineType() {
        return cuisineType;
    }

    /**
     * Returns the list of images related to a FoodTruck.
     *
     * @return the name of the FoodTruck.
     */
    public List<Image> getPhotos() {
        return photos;
    }

    /**
     * Returns the reviews of a FoodTruck.
     *
     * @return the reviews of the FoodTruck.
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Returns the menu of a FoodTruck.
     *
     * @return the menu of the FoodTruck.
     */
    public List<MenuItem> getMenu() {
        return menu;
    }
}
