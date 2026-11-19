import entities.MenuItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MenuItemTest {
    @Test
    void toStringShowsTwoDecimalPlaces() {
        MenuItem item = new MenuItem("Taco", "Taco al Pastor", new BigDecimal("4.5"));

        assertEquals("Taco $4.50", item.toString());
    }
}