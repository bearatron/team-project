package interface_adapter.add_menu_item;

import use_case.add_menu_item.AddMenuItemOutputBoundary;
import use_case.add_menu_item.AddMenuItemOutputData;

/**
 * The presenter for the Add Menu Item use case.
 */
public class AddMenuItemPresenter implements AddMenuItemOutputBoundary {
    private final AddMenuItemViewModel viewModel;

    /**
     * Creates a new AddMenuItemPresenter.
     *
     * @param viewModel the Add Menu Item view model
     */
    public AddMenuItemPresenter(AddMenuItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddMenuItemOutputData outputData) {
        final AddMenuItemState state = viewModel.getState();
        state.setSuccessMessage(String.format("Added %s ($%.2f)",
                outputData.getName(), outputData.getPrice()));
        state.setErrorMessage(null);
        state.setName("");
        state.setPrice("");
        state.setDescription("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AddMenuItemState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
