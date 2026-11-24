package entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodTruckTest {

    @Test
    void updateLocationChangesCoordinates() {
        FoodTruck truck = new FoodTruck(
                "Test Truck",
                new Location(0.0, 0.0),
                List.of("Mexican"),
                List.of()
        );

        truck.updateLocation(new Location(43.66, -79.39));

        assertEquals(43.66, truck.getLocation().getLatitude(), 1e-9);
        assertEquals(-79.39, truck.getLocation().getLongitude(), 1e-9);
    }

    @Test
    void addMenuItemAddsToMenu() {
        FoodTruck truck = new FoodTruck(
                "Test Truck", new Location(0.0, 0.0), List.of("Mexican"), List.of());
        MenuItem taco = new MenuItem("Taco", "Beef taco", new BigDecimal("4.50"));

        truck.addMenuItem(taco);

        assertEquals(1, truck.getMenu().size());
        assertEquals("Taco", truck.getMenu().getFirst().getName());
    }

    @Test
    void addMenuItemRejectsDuplicateName() {
        FoodTruck truck = new FoodTruck(
                "Test Truck", new Location(0.0, 0.0), List.of("Mexican"), List.of());
        truck.addMenuItem(new MenuItem("Taco", "Beef taco", new BigDecimal("4.50")));

        assertThrows(IllegalArgumentException.class, () ->
                truck.addMenuItem(new MenuItem("taco", "Chicken taco", new BigDecimal("5.00"))));
    }
}