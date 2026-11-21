package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ReplyTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 21, 12, 0);

    @Test
    void storesTextAndTimestamp() {
        Reply reply = new Reply("Thanks for visiting!", NOW);

        assertEquals("Thanks for visiting!", reply.getText());
        assertEquals(NOW, reply.getTimestamp());
    }

    @Test
    void rejectsBlankText() {
        assertThrows(IllegalArgumentException.class, () -> new Reply("   ", NOW));
        assertThrows(IllegalArgumentException.class, () -> new Reply("", NOW));
    }

    @Test
    void rejectsNulls() {
        assertThrows(NullPointerException.class, () -> new Reply(null, NOW));
        assertThrows(NullPointerException.class, () -> new Reply("Thanks", null));
    }

    @Test
    void equalityUsesTextAndTimestamp() {
        assertEquals(new Reply("Thanks", NOW), new Reply("Thanks", NOW));
        assertEquals(new Reply("Thanks", NOW).hashCode(), new Reply("Thanks", NOW).hashCode());
        assertNotEquals(new Reply("Thanks", NOW), new Reply("Cheers", NOW));
        assertNotEquals(new Reply("Thanks", NOW), new Reply("Thanks", NOW.plusMinutes(1)));
    }
}
