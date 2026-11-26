package interface_adapter.add_menu_item;

import use_case.add_menu_item.AddMenuItemInputBoundary;
import use_case.add_menu_item.AddMenuItemInputData;

/**
 * The controller for the Add Menu Item use case.
 */
public class AddMenuItemController {
    private final AddMenuItemInputBoundary interactor;

    /**
     * Creates a new AddMenuItemController.
     *
     * @param interactor the Add Menu Item interactor
     */
    public AddMenuItemController(AddMenuItemInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Runs the Add Menu Item use case with what the user entered.
     *
     * @param username the logged-in user
     * @param name the item name
     * @param price the price as typed
     * @param description the optional description
     */
    public void execute(String username, String name, String price, String description) {
        final AddMenuItemInputData inputData =
                new AddMenuItemInputData(username, name, price, description);
        interactor.execute(inputData);
    }
}