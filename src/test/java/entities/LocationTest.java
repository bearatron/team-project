package entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationTest {

    @Test
    void acceptsBoundaryValues() {
        assertDoesNotThrow(() -> new Location(90.0, 180.0));
        assertDoesNotThrow(() -> new Location(-90.0, -180.0));
    }

    @Test
    void rejectsLatitudeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(90.1, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Location(-90.1, 0.0));
    }

    @Test
    void rejectsLongitudeOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new Location(0.0, 180.1));
        assertThrows(IllegalArgumentException.class, () -> new Location(0.0, -180.1));
    }
}
