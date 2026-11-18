import entities.MenuItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MenuItemTest {

    @Test
    void toStringPadsNameAndFormatsPrice() {
        MenuItem item = new MenuItem("Shawarma","Chicken Platter", new BigDecimal("3.00"));

        String expected = "Shawarma" + " ".repeat(12) + " $  3.00";

        assertEquals(expected, item.toString());
    }

    @Test
    void toStringShowsTwoDecimalPlaces() {
        MenuItem item = new MenuItem("Taco", "Taco al Pastor", new BigDecimal("4.5"));

        assertEquals("Taco" + " ".repeat(16) + " $  4.50", item.toString());
    }
}