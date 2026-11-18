import entities.Location;
import entities.Review;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Comparator;

public class FoodTruck {
    private final String name;
    private final Location location;
    private final List<String> cuisineType;
    private final List<Image> photos;
    private final List<MenuItem> menu = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();

    public FoodTruck(String name, Location location, List<String> cuisineType, List<Image> photos) {
        this.name = name;
        this.location = location;
        this.cuisineType = new ArrayList<>(cuisineType);
        this.photos = new ArrayList<>(photos);
    }

    public void addItem(MenuItem item) {
        menu.add(item);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

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

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getCuisineType() {
        return cuisineType;
    }

    public List<Image> getPhotos() {
        return photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }
}