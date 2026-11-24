package use_case.add_menu_item;

/**
 * The input data for the Add Menu Item use case.
 */
public class AddMenuItemInputData {
    private final String username;
    private final String name;
    private final String price;
    private final String description;

    /**
     * Creates the input data for adding a menu item.
     *
     * @param username the logged-in user
     * @param name the item name
     * @param price the price as typed by the user
     * @param description the optional description
     */
    public AddMenuItemInputData(String username, String name,
                                String price, String description) {
        this.username = username;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}