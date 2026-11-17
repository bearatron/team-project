import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MenuItemTest {

    @Test
    void toStringPadsNameAndFormatsPrice() {
        MenuItem item = new MenuItem("Shawarma", new BigDecimal("3.00"), "Chicken Platter");

        String expected = "Shawarma" + " ".repeat(12) + " $  3.00";

        assertEquals(expected, item.toString());
    }

    @Test
    void toStringShowsTwoDecimalPlaces() {
        MenuItem item = new MenuItem("Taco", new BigDecimal("4.5"), "Taco al Pastor");

        assertEquals("Taco" + " ".repeat(16) + " $  4.50", item.toString());
    }
}