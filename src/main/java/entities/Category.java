package entities;

/**
 * An aspect of a food truck that a reviewer can rate.
 *
 * <p>In-app reviews rate every category; reviews imported from Google Maps
 * rate only {@link #OVERALL}.
 */
public enum Category {
    /** The reviewer's overall impression. */
    OVERALL,
    /** How the staff treated the customer. */
    SERVICE,
    /** How good the food was. */
    FOOD_QUALITY,
    /** Whether the food was worth its price. */
    VALUE_FOR_MONEY
}
