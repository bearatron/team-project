package app;

import data_access.InMemoryFoodTruckDataAccessObject;
import entities.FoodTruck;
import entities.Location;
import interface_adapter.add_menu_item.AddMenuItemController;
import interface_adapter.add_menu_item.AddMenuItemPresenter;
import interface_adapter.add_menu_item.AddMenuItemViewModel;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import use_case.add_menu_item.AddMenuItemInteractor;
import view.AddMenuItemView;

/**
 * A temporary demo that opens the Add Menu Item view.
 */
public final class AddMenuItemDemo {

    private AddMenuItemDemo() {
    }

    /**
     * Opens the Add Menu Item view in a window.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Fake data: one owner with one truck
            final InMemoryFoodTruckDataAccessObject dataAccess =
                    new InMemoryFoodTruckDataAccessObject();
            dataAccess.addTruck("owner", new FoodTruck("Food Truck 1",
                    new Location(43.66, -79.39), List.of("Korean"), List.of()));

            // Build each layer and connect them
            final AddMenuItemViewModel viewModel = new AddMenuItemViewModel();
            final AddMenuItemView view = new AddMenuItemView(viewModel);
            final AddMenuItemPresenter presenter = new AddMenuItemPresenter(viewModel);
            final AddMenuItemInteractor interactor =
                    new AddMenuItemInteractor(dataAccess, presenter);
            view.setController(new AddMenuItemController(interactor));
            view.setUsername("owner");

            // Show it
            final JFrame frame = new JFrame("Add Menu Item Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(view);
            frame.pack();
            frame.setVisible(true);
        });
    }
}