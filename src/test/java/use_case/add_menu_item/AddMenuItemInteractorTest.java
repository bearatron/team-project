package use_case.add_menu_item;

import entities.FoodTruck;
import entities.Location;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddMenuItemInteractorTest {

    private FoodTruck truck;
    private FakeDataAccess dataAccess;
    private FakePresenter presenter;
    private AddMenuItemInteractor interactor;

    @BeforeEach
    void setUp() {
        truck = new FoodTruck("Test Truck", new Location(0.0, 0.0),
                List.of("Mexican"), List.of());
        dataAccess = new FakeDataAccess(truck);
        presenter = new FakePresenter();
        interactor = new AddMenuItemInteractor(dataAccess, presenter);
    }

    @Test
    void successAddsItem() {
        interactor.execute(new AddMenuItemInputData("owner", "Taco", "4.50", "Beef taco"));

        assertNull(presenter.error);
        assertEquals("Taco", presenter.output.getName());
        assertEquals(1, truck.getMenu().size());
        assertTrue(dataAccess.saved);
    }

    @Test
    void failsWhenNoTruck() {
        dataAccess.truck = null;
        interactor.execute(new AddMenuItemInputData("owner", "Taco", "4.50", ""));
        assertEquals("You must be an owner with a registered truck.", presenter.error);
    }

    @Test
    void failsWhenNameEmpty() {
        interactor.execute(new AddMenuItemInputData("owner", "  ", "4.50", ""));
        assertEquals("Item name cannot be empty.", presenter.error);
    }

    @Test
    void failsWhenPriceNotNumber() {
        interactor.execute(new AddMenuItemInputData("owner", "Taco", "abc", ""));
        assertEquals("Price must be a number.", presenter.error);
    }

    @Test
    void failsWhenPriceZero() {
        interactor.execute(new AddMenuItemInputData("owner", "Taco", "0", ""));
        assertEquals("Price must be greater than zero.", presenter.error);
    }

    @Test
    void failsWhenDuplicateName() {
        interactor.execute(new AddMenuItemInputData("owner", "Taco", "4.50", ""));
        interactor.execute(new AddMenuItemInputData("owner", "taco", "5.00", ""));
        assertEquals("An item named \"taco\" already exists.", presenter.error);
    }

    /** A fake data access object that stores one truck in memory. */
    private static class FakeDataAccess implements AddMenuItemDataAccessInterface {
        private FoodTruck truck;
        private boolean saved;

        FakeDataAccess(FoodTruck truck) {
            this.truck = truck;
        }

        @Override
        public FoodTruck getTruckByOwner(String username) {
            return truck;
        }

        @Override
        public void save(FoodTruck foodTruck) {
            saved = true;
        }
    }

    /** A fake presenter that records what the interactor sent it. */
    private static class FakePresenter implements AddMenuItemOutputBoundary {
        private AddMenuItemOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(AddMenuItemOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
