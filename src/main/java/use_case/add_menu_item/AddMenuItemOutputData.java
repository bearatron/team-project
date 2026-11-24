package use_case.add_menu_item;

import java.math.BigDecimal;

/**
 * The output data for the Add Menu Item use case.
 */
public class AddMenuItemOutputData {
    private final String name;
    private final String description;
    private final BigDecimal price;

    /**
     * Creates the output data for an added menu item.
     *
     * @param name the item name
     * @param description the item description
     * @param price the item price
     */
    public AddMenuItemOutputData(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
