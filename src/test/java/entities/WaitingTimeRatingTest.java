package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class WaitingTimeRatingTest {

    @ParameterizedTest
    @CsvSource({
        "1, 20+ min",
        "2, about 15 min",
        "3, about 10 min",
        "4, 5-10 min",
        "5, under 5 min",
    })
    void labelsEachScore(int score, String label) {
        WaitingTimeRating rating = new WaitingTimeRating(score);

        assertEquals(label, rating.getLabel());
        assertEquals(label, WaitingTimeRating.labelFor(score));
    }

    @Test
    void categoryIsAlwaysWaitingTime() {
        assertEquals(Category.WAITING_TIME, new WaitingTimeRating(3).getCategory());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void rejectsScoresOutOfRange(int score) {
        assertThrows(IllegalArgumentException.class, () -> new WaitingTimeRating(score));
        assertThrows(IllegalArgumentException.class, () -> WaitingTimeRating.labelFor(score));
    }

    @Test
    void toStringIncludesLabel() {
        assertEquals("WAITING_TIME=5 (under 5 min)", new WaitingTimeRating(5).toString());
    }
}
