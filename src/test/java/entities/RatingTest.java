package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class RatingTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void acceptsScoresInRange(int score) {
        Rating rating = new Rating(Category.SERVICE, score);

        assertEquals(Category.SERVICE, rating.getCategory());
        assertEquals(score, rating.getScore());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6, -1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void rejectsScoresOutOfRange(int score) {
        assertThrows(IllegalArgumentException.class, () -> new Rating(Category.SERVICE, score));
    }

    @Test
    void rejectsNullCategory() {
        assertThrows(NullPointerException.class, () -> new Rating(null, 3));
    }

    @Test
    void rejectsPlainWaitingTimeRating() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(Category.WAITING_TIME, 3));
    }

    @ParameterizedTest
    @EnumSource(value = Category.class, names = "WAITING_TIME", mode = EnumSource.Mode.EXCLUDE)
    void factoryReturnsPlainRatingForOtherCategories(Category category) {
        Rating rating = Rating.of(category, 4);

        assertEquals(Rating.class, rating.getClass());
        assertEquals(category, rating.getCategory());
        assertEquals(4, rating.getScore());
    }

    @Test
    void factoryReturnsWaitingTimeRatingForWaitingTime() {
        Rating rating = Rating.of(Category.WAITING_TIME, 2);

        assertInstanceOf(WaitingTimeRating.class, rating);
        assertEquals(2, rating.getScore());
    }

    @Test
    void equalityUsesCategoryAndScore() {
        assertEquals(new Rating(Category.CLEANLINESS, 3), new Rating(Category.CLEANLINESS, 3));
        assertEquals(new Rating(Category.CLEANLINESS, 3).hashCode(),
                new Rating(Category.CLEANLINESS, 3).hashCode());
        assertNotEquals(new Rating(Category.CLEANLINESS, 3), new Rating(Category.CLEANLINESS, 4));
        assertNotEquals(new Rating(Category.CLEANLINESS, 3), new Rating(Category.SPEED, 3));
    }

    @Test
    void waitingTimeRatingsFromConstructorAndFactoryAreEqual() {
        assertEquals(new WaitingTimeRating(5), Rating.of(Category.WAITING_TIME, 5));
    }
}
