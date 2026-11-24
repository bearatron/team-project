package entities;

import org.junit.jupiter.api.Test;

import java.util.List;

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
}