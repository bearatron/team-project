package use_case.add_menu_item;

import entities.FoodTruck;
import entities.MenuItem;
import java.math.BigDecimal;

/**
 * The interactor for the Add Menu Item use case.
 */
public class AddMenuItemInteractor implements AddMenuItemInputBoundary {
    private final AddMenuItemDataAccessInterface dataAccess;
    private final AddMenuItemOutputBoundary presenter;

    /**
     * Creates a new AddMenuItemInteractor.
     *
     * @param dataAccess the data access object
     * @param presenter the output boundary
     */
    public AddMenuItemInteractor(AddMenuItemDataAccessInterface dataAccess,
                                 AddMenuItemOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddMenuItemInputData inputData) {
        // 1. Find the owner's truck
        final FoodTruck truck = dataAccess.getTruckByOwner(inputData.getUsername());
        if (truck == null) {
            presenter.prepareFailView("You must be an owner with a registered truck.");
            return;
        }

        // 2. Validate the name
        final String name = inputData.getName() == null ? "" : inputData.getName().trim();
        if (name.isEmpty()) {
            presenter.prepareFailView("Item name cannot be empty.");
            return;
        }

        // 3. Validate the price
        final BigDecimal price = parsePrice(inputData.getPrice());
        if (price == null) {
            presenter.prepareFailView("Price must be a number.");
            return;
        }
        if (price.signum() <= 0) {
            presenter.prepareFailView("Price must be greater than zero.");
            return;
        }

        // 4. Check for a duplicate name
        for (MenuItem existing : truck.getMenu()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                presenter.prepareFailView("An item named \"" + name + "\" already exists.");
                return;
            }
        }

        // 5. Add the item, save, and report success
        String description = inputData.getDescription();
        if (description == null) {
            description = "";
        }
        truck.addMenuItem(new MenuItem(name, description, price));
        dataAccess.save(truck);

        presenter.prepareSuccessView(new AddMenuItemOutputData(name, description, price));
    }

    /**
     * Converts the price text to a number.
     *
     * @param priceText the price as typed by the user
     * @return the price, or null if it is missing or not a number
     */
    private BigDecimal parsePrice(String priceText) {
        if (priceText == null) {
            return null;
        }
        try {
            return new BigDecimal(priceText.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}