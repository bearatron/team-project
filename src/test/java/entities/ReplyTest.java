package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ReplyTest {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2026, 11, 21, 12, 0);

    @Test
    void storesTextAndTimestamp() {
        Reply reply = new Reply("Thanks for visiting!", TEST_DATE);

        assertEquals("Thanks for visiting!", reply.getText());
        assertEquals(TEST_DATE, reply.getTimestamp());
    }

    @Test
    void rejectsBlankText() {
        assertThrows(IllegalArgumentException.class, () -> new Reply("   ", TEST_DATE));
        assertThrows(IllegalArgumentException.class, () -> new Reply("", TEST_DATE));
    }

    @Test
    void rejectsNulls() {
        assertThrows(NullPointerException.class, () -> new Reply(null, TEST_DATE));
        assertThrows(NullPointerException.class, () -> new Reply("Thanks", null));
    }

    @Test
    void equalityUsesTextAndTimestamp() {
        assertEquals(new Reply("Thanks", TEST_DATE), new Reply("Thanks", TEST_DATE));
        assertEquals(new Reply("Thanks", TEST_DATE).hashCode(), new Reply("Thanks", TEST_DATE).hashCode());
        assertNotEquals(new Reply("Thanks", TEST_DATE), new Reply("Cheers", TEST_DATE));
        assertNotEquals(new Reply("Thanks", TEST_DATE), new Reply("Thanks", TEST_DATE.plusMinutes(1)));
    }
}
