package entities;

import java.math.BigDecimal;

/**
 * Represents a PriceRange for the difference in the max and min prices on a Menu.
 */
public class PriceRange {
    private final BigDecimal min;
    private final BigDecimal max;

    /**
     * Creates a PriceRange from min to max on a given Menu.
     *
     * @param min The name of the MenuItem.
     * @param max The description of the MenuItem.
     */
    public PriceRange(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the min price of a FoodTruck's menu.
     *
     * @return the min price of the FoodTruck's menu.
     */
    public BigDecimal getMin() {
        return min;
    }

    /**
     * Returns the max price of a FoodTruck's menu.
     *
     * @return the max price of the FoodTruck's menu.
     */
    public BigDecimal getMax() {
        return max;
    }

    @Override
    public String toString() {
        return String.format("$%.2f - $%.2f", min, max);
    }
}
