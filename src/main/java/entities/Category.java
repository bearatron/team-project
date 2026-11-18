package entities;

/**
 * An aspect of a food truck that a reviewer can rate.
 *
 * <p>A review may rate any subset of these categories, but at most once each.
 * {@link #WAITING_TIME} ratings are always represented by {@link WaitingTimeRating}.
 */
public enum Category {
    OVERALL,
    SERVICE,
    FOOD_QUALITY,
    VALUE_FOR_MONEY,
    WAITING_TIME,
    CLEANLINESS,
    SPEED
}
