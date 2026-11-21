package entities;

import java.math.BigDecimal;

/**
 * Returns a MenuItem on a menu for a given FoodTruck.
 */
public class MenuItem {
    private final String name;
    private final String description;
    private final BigDecimal price;

    /**
     * Creates a new FoodTruck with the given name, location, cuisine, and
     * optional photos to be uploaded.
     *
     * @param name The name of the MenuItem.
     * @param description The description of the MenuItem.
     * @param price The price of the MenuItem.
     */
    public MenuItem(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Return the MenuItem from the menu of a FoodTruck.
     *
     * @return the name of the MenuItem.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the price of a certain MenuItem from the menu of a FoodTruck.
     *
     * @return the price of the MenuItem.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Return the description of a MenuItem from the menu of a FoodTruck.
     *
     * @return the description of the MenuItem.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }
}
