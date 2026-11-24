package view;

import interface_adapter.add_menu_item.AddMenuItemController;
import interface_adapter.add_menu_item.AddMenuItemState;
import interface_adapter.add_menu_item.AddMenuItemViewModel;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The view for adding a menu item to a food truck.
 */
public class AddMenuItemView extends JPanel implements PropertyChangeListener {
    private static final int FIELD_WIDTH = 20;
    private static final Color SUCCESS_GREEN = new Color(0, 128, 0);

    private final AddMenuItemViewModel viewModel;
    private AddMenuItemController controller;
    private String username = "";

    private final JTextField nameField = new JTextField(FIELD_WIDTH);
    private final JTextField priceField = new JTextField(FIELD_WIDTH);
    private final JTextField descriptionField = new JTextField(FIELD_WIDTH);
    private final JLabel messageLabel = new JLabel(" ");

    /**
     * Creates the Add Menu Item view.
     *
     * @param viewModel the Add Menu Item view model
     */
    public AddMenuItemView(AddMenuItemViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        final JPanel form = new JPanel(new GridLayout(0, 2));
        form.add(new JLabel("Item name:"));
        form.add(nameField);
        form.add(new JLabel("Price:"));
        form.add(priceField);
        form.add(new JLabel("Description (optional):"));
        form.add(descriptionField);

        JButton addButton = new JButton("Add item");
        addButton.addActionListener(event -> onAddClicked());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Add a menu item"));
        add(form);
        add(addButton);
        add(messageLabel);
    }

    /**
     * Saves what the user typed into the state, then runs the use case.
     */
    private void onAddClicked() {
        if (controller != null) {
            final AddMenuItemState current = viewModel.getState();
            current.setName(nameField.getText());
            current.setPrice(priceField.getText());
            current.setDescription(descriptionField.getText());

            controller.execute(username, nameField.getText(),
                    priceField.getText(), descriptionField.getText());
        }
    }

    /**
     * Sets the controller this view sends user actions to.
     *
     * @param controller the Add Menu Item controller
     */
    public void setController(AddMenuItemController controller) {
        this.controller = controller;
    }

    /**
     * Sets the logged-in user whose truck items are added to.
     *
     * @param username the logged-in user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AddMenuItemState state = viewModel.getState();
        nameField.setText(state.getName());
        priceField.setText(state.getPrice());
        descriptionField.setText(state.getDescription());

        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(state.getErrorMessage());
        } else if (state.getSuccessMessage() != null) {
            messageLabel.setForeground(SUCCESS_GREEN);
            messageLabel.setText(state.getSuccessMessage());
        } else {
            messageLabel.setText(" ");
        }
    }
}