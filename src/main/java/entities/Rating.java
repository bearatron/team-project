package entities;

import java.util.Objects;

/**
 * An immutable score from 1 to 5 for one {@link Category} of a food truck.
 *
 * <p>Waiting-time scores carry human-readable labels, so a {@link Category#WAITING_TIME}
 * rating must be a {@link WaitingTimeRating}. Use {@link #of(Category, int)} to get the
 * right type for any category.
 */
public class Rating {

    /** The lowest allowed score. */
    public static final int MIN_SCORE = 1;

    /** The highest allowed score. */
    public static final int MAX_SCORE = 5;

    private final Category category;
    private final int score;

    /**
     * Creates a rating.
     *
     * @param category the rated category; not {@code null}
     * @param score the score, from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     * @throws NullPointerException if {@code category} is {@code null}
     * @throws IllegalArgumentException if {@code score} is out of range, or if
     *     {@code category} is {@link Category#WAITING_TIME} and this is not a
     *     {@link WaitingTimeRating}
     */
    public Rating(Category category, int score) {
        this.category = Objects.requireNonNull(category, "category");
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException(
                    "score must be between " + MIN_SCORE + " and " + MAX_SCORE + ", was " + score);
        }
        if (category == Category.WAITING_TIME && !(this instanceof WaitingTimeRating)) {
            throw new IllegalArgumentException(
                    "WAITING_TIME ratings must be created as WaitingTimeRating");
        }
        this.score = score;
    }

    /**
     * Creates a rating of the correct type for {@code category}.
     *
     * @param category the rated category; not {@code null}
     * @param score the score, from {@value #MIN_SCORE} to {@value #MAX_SCORE}
     * @return a {@link WaitingTimeRating} for {@link Category#WAITING_TIME}, otherwise a
     *     {@code Rating}
     */
    public static Rating of(Category category, int score) {
        if (category == Category.WAITING_TIME) {
            return new WaitingTimeRating(score);
        }
        return new Rating(category, score);
    }

    /** Returns the rated category. */
    public Category getCategory() {
        return category;
    }

    /** Returns the score, from {@value #MIN_SCORE} to {@value #MAX_SCORE}. */
    public int getScore() {
        return score;
    }

    /** Two ratings are equal when they have the same category and score. */
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rating)) {
            return false;
        }
        Rating that = (Rating) other;
        return category == that.category && score == that.score;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(category, score);
    }

    @Override
    public String toString() {
        return category + "=" + score;
    }
}
