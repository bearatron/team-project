package entities;

/**
 * An aspect of a food truck that a reviewer can rate.
 *
 * <p>A review may rate any subset of these categories, at most once each.
 */
public enum Category {
    /** The reviewer's overall impression. */
    OVERALL,
    /** How the staff treated the customer. */
    SERVICE,
    /** How good the food was. */
    FOOD_QUALITY,
    /** Whether the food was worth its price. */
    VALUE_FOR_MONEY,
    /** How long the customer waited for their order. */
    WAITING_TIME,
    /** How clean the truck and its surroundings were. */
    CLEANLINESS,
    /** How quickly the order was prepared. */
    SPEED
}
